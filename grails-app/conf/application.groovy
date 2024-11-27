environments {
    development {
        dataSource {
            dbCreate= "none" //dbCreate - one of 'create', 'create-drop', 'update', 'validate', ''
            url="jdbc:oracle:thin:@//localhost:1521/XEPDB1"
            // Oracle-Cloud
            // url="jdbc:oracle:thin:@luchocidatabase_medium?TNS_ADMIN=/Users/darthluch/Warez/OracleCloudWallet/2024-08-30-Oci/Wallet_LuchOciDatabase"
            username="LUCH"
            password="LUCH"
            //password="b36F25gfvYtTL" - Oracle-cloud for DarthLUch
            //password="EOTzekjgb1siVA" - Oracle-cloud for ObjCmptng
            logSql= true;
        }
    }
    test {
        dataSource {
            dbCreate= "none"
            //dbCreate - one of 'create', 'create-drop', 'update', 'validate', ''
            url="jdbc:oracle:thin:@(DESCRIPTION =    (ADDRESS = (PROTOCOL = TCP)(HOST = localhost)(PORT = 1521))    (CONNECT_DATA =      (SERVER = DEDICATED)      (SID = local)    )  )"
            username= "BLUE"
            password= "BLUE"
            logSql= false
        }
    }
    production {
        dataSource {
            dbCreate= "none" //dbCreate - one of 'create', 'create-drop', 'update', 'validate', ''
            // url="jdbc:oracle:thin:@//host.containers.internal:1521/XEPDB1"
            // Oracle-Cloud
            url="jdbc:oracle:thin:@luchocidatabase_medium?TNS_ADMIN=/home/ubuntu/Wallet_LuchOciDatabase"
            username="luch"
            //password="luch"
            //password="b36F25gfvYtTL" //Oracle-cloud for DarthLUch
            password="EOTzekjgb1siVA" //Oracle-cloud for ObjCmptng
            logSql= true
            properties {
                validationQuery = "SELECT 1 FROM DUAL"
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                timeBetweenEvictionRunsMillis = 5000
                minEvictableIdleTimeMillis = 60000
                maxAge = 10 * 60000
                maxWait = 10000
                maxIdle = 25
                maxActive = 50
                initialSize = 5
            }
        }
    }
}


// Production for Tomcat/JNDI
/*
production {
    dataSource {
        dbCreate = "none"
        resourceRef="true"
        pooled = true
        logSql= false
        //jndiName = "java:/comp/env/jdbc/AtosGrailsSix"
        //jndiName = "jdbc/AtosGrailsSix"
        //jndiName = "${System.getProperty('catalina.home') ? 'java:comp/env/' : ''}jdbc/AtosGrailsSix"
        jndiName = "${(System.getProperty('catalina.home') && (System.getProperty('java.class.path')).trim().toLowerCase().indexOf('tomcat') > 0 ) ? 'java:comp/env/' : ''}jdbc/AtosGrailsSix"
        properties {
            //maxActive = -1
            //minEvictableIdleTimeMillis=1800000
            //timeBetweenEvictionRunsMillis=1800000
            //numTestsPerEvictionRun=3
            //testOnBorrow=true
            //testWhileIdle=true
            //testOnReturn=true
            //validationQuery="SELECT 1 from dual"
            jmxEnabled= true
            initialSize= 5
            maxActive= 50
            minIdle= 5
            maxIdle= 25
            maxWait= 10000
            maxAge= 600000
            timeBetweenEvictionRunsMillis= 5000
            minEvictableIdleTimeMillis= 60000
            validationQuery= "SELECT 1 from dual"
            validationQueryTimeout= 3
            validationInterval= 15000
            testOnBorrow= true
            testWhileIdle= true
            testOnReturn= false
            jdbcInterceptors= "ConnectionState"
            defaultTransactionIsolation= 2 //TRANSACTION_READ_COMMITTED
        }
    }
}
*/
