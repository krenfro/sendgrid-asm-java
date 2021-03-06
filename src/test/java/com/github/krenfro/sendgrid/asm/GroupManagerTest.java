package com.github.krenfro.sendgrid.asm;

import java.io.IOException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupManagerTest extends AbstractTest {

    public static final String TEST_GROUP_NAME = "junit-group";

    @BeforeClass
    @AfterClass
    public static void cleanup() throws IOException{
        GroupManager groups = getGroupManager();
        for (Group group: groups.retrieve()){
            if (group.getName().equals(TEST_GROUP_NAME)){
                groups.remove(group);
                break;
            }
        }
    }

    @Test
    public void testOperations() throws IOException{
        GroupManager groups = getGroupManager();
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
