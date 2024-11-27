package com.luch.trait

import java.sql.Clob

trait ExcelImport {
    String createdBy;
    Date createdDate;
    Integer id;
    Integer sheetNo;
    Integer rowExcel;
    String columnExcel;
    String typeAttribute;
    Clob value;
}
