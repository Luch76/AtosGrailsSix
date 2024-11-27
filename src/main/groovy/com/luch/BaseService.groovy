package com.luch

import javax.servlet.http.HttpSession
import javax.sql.DataSource

abstract class BaseService {
    DataSource dataSource;
    HttpSession httpSession;

    def init(DataSource dataSourceA, HttpSession httpSessionA)  {
        this.dataSource = dataSourceA;
        this.httpSession = httpSessionA;
    }
}
