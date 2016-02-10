package com.github.krenfro.sendgrid.asm;

import java.io.IOException;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class SuppressionManagerTest extends AbstractTest  {

    public static final String TEST_GROUP_NAME = "junit-suppression-group";
    public static final String TEST_EMAIL = "junit@does-not-exist.com";

    @BeforeClass
    @AfterClass
    public static void cleanup() throws IOException{
        GroupManager groups = getGroupManager();
        SuppressionManager suppressionManager = getSuppressionManager();
        List<Suppression> suppressions = suppressionManager.retrieve(TEST_EMAIL);
        for (Suppression suppression: suppressions){
            suppression.setSuppressed(false);
        }
        suppressionManager.save(TEST_EMAIL, suppressions);
        for (Group group: groups.retrieve()){
            if (group.getName().equals(TEST_GROUP_NAME)){
                groups.remove(group);
                break;
            }
        }
    }

    @Test
    public void testOperations() throws IOException{
        GroupManager groupManager = getGroupManager();
        Group group = groupManager.add(TEST_GROUP_NAME, "test group");
        SuppressionManager suppressionManager = getSuppressionManager();
        List<Suppression> list = suppressionManager.retrieve(TEST_EMAIL);
        assertFalse(list.isEmpty());
        for (Suppression suppression: list){
            assertFalse(suppression.isSuppressed());
        }
        assertTrue(suppressionManager.retrieve(group).isEmpty());
        suppressionManager.remove(group, TEST_EMAIL);

        //add a suppression
        List<String> addresses = suppressionManager.add(group, TEST_EMAIL);
        assertFalse(addresses.isEmpty());
        assertTrue(addresses.contains(TEST_EMAIL));
        assertTrue(suppressionManager.retrieve(group).contains(TEST_EMAIL));

        //ensure suppression is there
        List<Suppression> suppressions = suppressionManager.retrieve(TEST_EMAIL);
        for (Suppression s: suppressions){
            if (s.getId().equals(group.getId())){
                assertTrue(s.isSuppressed());
                s.setSuppressed(false);
            }
            else{
                assertFalse(s.isSuppressed());
            }
        }

        //all suppressions should be false
        suppressionManager.save(TEST_EMAIL, suppressions);
        List<Suppression> updated = suppressionManager.retrieve(TEST_EMAIL);
        for (Suppression previous: suppressions){
            for (Suppression update: updated){
                if (previous.getId().equals(update.getId())){
                    assertFalse(previous.isSuppressed());
                }
            }
        }

        suppressionManager.remove(group, TEST_EMAIL);
        assertFalse(suppressionManager.retrieve(group).contains(TEST_EMAIL));
        groupManager.remove(group);
    }

}
