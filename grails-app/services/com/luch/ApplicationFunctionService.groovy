package com.luch


import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import groovy.sql.Sql
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest

interface IApplicationFunctionService {

}

@Transactional
@Service(ApplicationFunctionService)
abstract class ApplicationFunctionService extends BaseService implements IApplicationFunctionService {

    LuchSessionService luchSessionService;

    String uploadFileSession(HttpServletRequest request, String getFile) {
        MultipartFile uploadedFile;
        String excelFileLocation;
        File fileDelete;

        uploadedFile = request.getFile(getFile);
        if (uploadedFile.empty) {
            throw new Exception ("No file was selected - Select a file then click Upload button.");
        }
        if(!uploadedFile.empty){
            log.debug "Class: ${uploadedFile.class}"
            log.debug "Name: ${uploadedFile.name}"
            log.debug "OriginalFileName: ${uploadedFile.originalFilename}"
            log.debug "Size: ${uploadedFile.size}"
            log.debug "ContentType: ${uploadedFile.contentType}"
        }

        this.httpSession.tempFolderFullPath = luchSessionService.sessionTempFolder(this.httpSession);
        if (uploadedFile && !uploadedFile.empty) {
            excelFileLocation = this.httpSession.tempFolderFullPath + "/" + new File(uploadedFile.originalFilename).getName();
            fileDelete = new File(excelFileLocation);
            if (fileDelete.exists()) {
                fileDelete.delete();
            }
            uploadedFile.transferTo (new File(excelFileLocation));
        }
        log.info("excelFileLocation: " + excelFileLocation);
        return excelFileLocation;
    }

    Date currentDateTime() {
        Date currentDateTime;
        Sql sql;

        sql = new Sql(dataSource);

        sql.call("{$Sql.TIMESTAMP = call \"SYSDATE\"}") {
            returnedDateTime -> currentDateTime =  new Date(returnedDateTime.getTime());
        }
        sql.close();

        return currentDateTime

    }

    Integer getSequenceHibernate() {
        String sqlString;
        Sql sql;
        Integer nextVal;

        sql = new Sql(dataSource);
        sqlString = "SELECT HIBERNATE_SEQUENCE.NEXTVAL AS NEXT_VAL FROM DUAL ";

        sql.eachRow(sqlString) {
            nextVal = it."NEXT_VAL";
        }
        sql.close();

        return nextVal;
    }

    Integer getObjectCount(String objectType, String objectName) {
        String sqlString;
        Sql sql;
        Integer countTable;

        sql = new Sql(dataSource);
        sqlString = "SELECT COUNT(*) as MYCOUNT FROM USER_OBJECTS U WHERE 1=1 AND U.OBJECT_TYPE = :objectType AND U.OBJECT_NAME = :objectName ";

        sql.eachRow(sqlString, [objectType:objectType, objectName:objectName]) {
            countTable = it."MYCOUNT";
        }
        sql.close();

        return countTable;
    }

    Integer getTableCount(String tableName) {
        return getObjectCount("TABLE", tableName);
    }

}
