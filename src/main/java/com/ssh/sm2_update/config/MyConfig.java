package com.ssh.sm2_update.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "myProps")
public class MyConfig {

    private String serviceIp;

    private String userDB;

    private String contentDB;

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public String getUserDB() {
        return userDB;
    }

    public void setUserDB(String userDB) {
        this.userDB = userDB;
    }

    public String getContentDB() {
        return contentDB;
    }

    public void setContentDB(String contentDB) {
        this.contentDB = contentDB;
    }
}
