package com.github.krenfro.sendgrid.asm;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupsTest {

    
    public static final String USERNAME = System.getProperty("sendgrid-username");
    public static final String PASSWORD = System.getProperty("sendgrid-password");
    
    public static final String TEST_GROUP_NAME = "junit-group";        
    
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
        Groups groups = new Groups(USERNAME, PASSWORD);
        Group group = groups.add(TEST_GROUP_NAME, "test group");
        assertNotNull(group.getId());
        assertEquals(TEST_GROUP_NAME, group.getName());
        assertEquals("test group", group.getDescription());
        assertTrue(groups.retrieve().contains(group));
        /* update not supported until Apache HttpClient dependency is >= 4.4
        group.setDescription("modified description");
        groups.update(group);
        group = groups.retrieve(group.getId());
        assertEquals(TEST_GROUP_NAME, group.getName());
        assertEquals("modified description", group.getDescription());
        */
        groups.remove(group);
        assertFalse(groups.retrieve().contains(group));
    }

}
