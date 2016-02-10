package com.github.krenfro.sendgrid.asm;

import java.io.IOException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public abstract class AbstractTest {

    /* When running these tests, use Java environment variables to set credentials one of two ways:
        mvn -Dsendgrid-username=ABC -Dsendgrid-password=XXXXX clean test
        mvn -Dsendgrid-apikey=XXXXX clean test
    */
    public static final String API_KEY = System.getProperty("sendgrid-apikey");
    public static final String USERNAME = System.getProperty("sendgrid-username");
    public static final String PASSWORD = System.getProperty("sendgrid-password");

    public static GroupManager getGroupManager() {
        if(API_KEY != null && !API_KEY.isEmpty()) {
            return new GroupManager(API_KEY);
        } else {
            return new GroupManager(USERNAME, PASSWORD);
        }
    }

    public static GlobalSuppressionManager getGlobalSuppressionManager() {
        if(API_KEY != null && !API_KEY.isEmpty()) {
            return new GlobalSuppressionManager(API_KEY);
        } else {
            return new GlobalSuppressionManager(USERNAME, PASSWORD);
        }
    }

    public static SuppressionManager getSuppressionManager() {
        if(API_KEY != null && !API_KEY.isEmpty()) {
            return new SuppressionManager(API_KEY);
        } else {
            return new SuppressionManager(USERNAME, PASSWORD);
        }
    }
}
