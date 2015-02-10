/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.aws.tests.glacier;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

public class DescriptionSearchTest
{
    private static final Logger logger = Logger.getLogger(DescriptionSearchTest.class.getName());

    @Test
    public void archiveDataTest()
    {
        try
        {
            AWSAPIProperties awsAPIProperties = new AWSAPIProperties("awsapi.properties");

            if (! awsAPIProperties.isLoaded())
            {
                logger.log(Level.INFO, "SKIPPING TEST 'archiveDataTest', no propertiles file");
                return;
            }
        }
        catch (Throwable throwable)
        {
            logger.log(Level.WARNING, "Problem in 'archiveDataTest'", throwable);
            fail("Problem in 'archiveDataTest': " + throwable);
        }
    }
}
