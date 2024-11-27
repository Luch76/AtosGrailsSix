package com.luch


import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

import javax.servlet.http.HttpSession

interface ILuchSessionService {

}

@Transactional
@Service(LuchSessionService)
abstract class LuchSessionService extends BaseService implements ILuchSessionService {

    String sessionTempFolder(HttpSession httpSession) {
        File fileTempFolder;
        String tempDir;

        if (httpSession?.tempFolderFullPath?.trim()?.length() > 0) {
            fileTempFolder = new File(httpSession?.tempFolderFullPath);
            log.info("LuchSessionService, sessionTempFolder, httpSession.tempFolderFullPath: " + httpSession.tempFolderFullPath);
        } else {
            tempDir = "AtosGrailsSix_Uuid_" + UUID.randomUUID().toString();
            log.info("LuchSessionService, sessionTempFolder, tempDir: " + tempDir);
            fileTempFolder = new File(System.getProperty("java.io.tmpdir"), tempDir);
        }
        if (!fileTempFolder.isDirectory()) {
            log.info("LuchSessionService, sessionTempFolder, Creating temp directory $fileTempFolder");
            fileTempFolder.mkdirs()
        }
        httpSession.tempFolderFullPath = fileTempFolder.getAbsolutePath();
        return httpSession.tempFolderFullPath;
    }

}
