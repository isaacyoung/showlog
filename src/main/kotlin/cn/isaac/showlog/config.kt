package cn.isaac.showlog

/**
 * 配置文件
 * create by isaac at 2018/1/23 10:50
 */
val config = configBuilder {
    server {
        title = "wxschool"
        host = "192.168.10.12"
        userName = "root"
        password = "schoolpay"
        logFile = "/usr/local/tomcat6/logs/catalina.out"
    }
}
