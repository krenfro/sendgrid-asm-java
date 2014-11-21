package com.github.krenfro.sendgrid.asm;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuppressionsTest {

    
    public static final String USERNAME = System.getProperty("sendgrid-username");
    public static final String PASSWORD = System.getProperty("sendgrid-password");
    
    public static final String TEST_GROUP_NAME = "junit-suppression-group";        
    
    @BeforeClass
    @AfterClass
    public static void removeTestGroup() throws IOException{        
        Groups groups = new Groups(USERNAME, PASSWORD);
        for (Group group: groups.retrieve()){
            if (group.getName().equals(TEST_GROUP_NAME)){
                groups.remove(group);
                break;
            }
        }
    }
    
    @Test
    public void testOperations() throws IOException{
        String email = "email@does-not-exist.com";
        Groups groups = new Groups(USERNAME, PASSWORD);
        Group group = groups.add(TEST_GROUP_NAME, "test group");
        Suppressions suppressions = new Suppressions(USERNAME, PASSWORD);
        assertTrue(suppressions.retrieve(email).isEmpty());
        assertTrue(suppressions.retrieve(group).isEmpty());
        suppressions.remove(group, email);
        List<String> addresses = suppressions.add(group, email);
        assertFalse(addresses.isEmpty());
        assertTrue(addresses.contains(email));
        assertTrue(suppressions.retrieve(group).contains(email));
        suppressions.remove(group, email);
        assertFalse(suppressions.retrieve(group).contains(email));
        groups.remove(group);
    }

}
