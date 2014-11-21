package com.github.krenfro.sendgrid.asm;

import java.io.IOException;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class GlobalSuppressionManagerTest {
    
    /* When running these tests, use Java environment variables to set credentials:
       e.g., "mvn -Dsendgrid-username=ABC -Dsendgrid-password=XXXXX clean test"    
    */    
    public static final String USERNAME = System.getProperty("sendgrid-username");
    public static final String PASSWORD = System.getProperty("sendgrid-password");    
    public static final String TEST_GROUP_NAME = "junit-suppression-group";               
    public static final String TEST_EMAIL = "junit@does-not-exist.com"; 
    
    @BeforeClass
    @AfterClass
    public static void cleanup() throws IOException{        
        GlobalSuppressionManager gsm = new GlobalSuppressionManager(USERNAME, PASSWORD);
        gsm.remove(TEST_EMAIL);
    }
    
    @Test
    public void testOperations() throws IOException{
        
        GlobalSuppressionManager gsm = new GlobalSuppressionManager(USERNAME, PASSWORD);
        assertFalse(gsm.exists(TEST_EMAIL));
        assertTrue(gsm.add(TEST_EMAIL).contains(TEST_EMAIL));
        assertTrue(gsm.exists(TEST_EMAIL));
        gsm.remove(TEST_EMAIL);
        assertFalse(gsm.exists(TEST_EMAIL));
    }

}