package com.github.krenfro.sendgrid.asm;

import java.io.IOException;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class GlobalSuppressionManagerTest extends AbstractTest {

    public static final String TEST_EMAIL = "junit@does-not-exist.com";

    @BeforeClass
    @AfterClass
    public static void cleanup() throws IOException{
        GlobalSuppressionManager gsm = getGlobalSuppressionManager();
        gsm.remove(TEST_EMAIL);
    }

    @Test
    public void testOperations() throws IOException{
        GlobalSuppressionManager gsm = getGlobalSuppressionManager();
        assertFalse(gsm.has(TEST_EMAIL));
        assertTrue(gsm.add(TEST_EMAIL).contains(TEST_EMAIL));
        assertTrue(gsm.has(TEST_EMAIL));
        gsm.remove(TEST_EMAIL);
        assertFalse(gsm.has(TEST_EMAIL));
    }

}
