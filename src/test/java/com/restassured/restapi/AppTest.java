package com.restassured.restapi;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static org.testng.Assert.assertTrue;
import static org.testng.TestNGAntTask.Mode.junit;
import static org.testng.TestNGAntTask.Mode.testng;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
