/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.aws.tests.glacier;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import static org.junit.Assert.*;

public class AWSAPIProperties
{
    public AWSAPIProperties(String awsAPIPropertiesFilename)
    {
        _awsAPIProperties = new Properties();

        try
        {
            FileReader awsAPIFileReader = new FileReader(awsAPIPropertiesFilename);
            _awsAPIProperties.load(awsAPIFileReader);
            awsAPIFileReader.close();
            _loaded = true;
        }
        catch (IOException ioException)
        {
            _awsAPIProperties = null;
            _loaded = false;
        }
    }

    public boolean isLoaded()
    {
        return _loaded;
    }

    public String getAWSAccessKeyID()
    {
        if (_awsAPIProperties != null)
        {
            String awsAccessKeyID = _awsAPIProperties.getProperty("awsaccesskeyid");

            if (awsAccessKeyID != null)
                return awsAccessKeyID;
            else
            {
                fail("Failed to obtain \"awsaccesskeyid\" property");
                return null;
            }
        }
        else
        {
            fail("Failed to obtain \"awsaccesskeyid\" property, no property file");
            return null;
        }
    }

    public String getAWSSecretAccessKey()
    {
        if (_awsAPIProperties != null)
        {
            String awsSecretAccessKey = _awsAPIProperties.getProperty("awssecretaccesskey");

            if (awsSecretAccessKey != null)
                return awsSecretAccessKey;
            else
            {
                fail("Failed to obtain \"awssecretaccesskey\" property");
                return null;
            }
        }
        else
        {
            fail("Failed to obtain \"awssecretaccesskey\" property, no property file");
            return null;
        }
    }

    public String getAWSAccountID()
    {
        if (_awsAPIProperties != null)
        {
            String awsAccountID = _awsAPIProperties.getProperty("awsaccountid");

            if (awsAccountID != null)
                return awsAccountID;
            else
            {
                fail("Failed to obtain \"awsaccountid\" property");
                return null;
            }
        }
        else
        {
            fail("Failed to obtain \"awsaccountid\" property, no property file");
            return null;
        }
    }

    public String getGlacierEndpointURL()
    {
        if (_awsAPIProperties != null)
        {
            String glacierEndpointURL = _awsAPIProperties.getProperty("glacierendpointurl");

            if (glacierEndpointURL != null)
                return glacierEndpointURL;
            else
            {
                fail("Failed to obtain \"glacierendpointurl\" property");
                return null;
            }
        }
        else
        {
            fail("Failed to obtain \"glacierendpointurl\" property, no property file");
            return null;
        }
    }

    public String getGlacierVaultName()
    {
        if (_awsAPIProperties != null)
        {
            String glacierVaultName = _awsAPIProperties.getProperty("glaciervaultname");

            if (glacierVaultName != null)
                return glacierVaultName;
            else
            {
                fail("Failed to obtain \"glaciervaultname\" property");
                return null;
            }
        }
        else
        {
            fail("Failed to obtain \"glaciervaultname\" property, no property file");
            return null;
        }
    }

    private boolean    _loaded;
    private Properties _awsAPIProperties = new Properties();
}
