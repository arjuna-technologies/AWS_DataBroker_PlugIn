/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.aws.glacier;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.arjuna.databroker.data.DataConsumer;
import com.arjuna.databroker.data.DataFlow;
import com.arjuna.databroker.data.DataProvider;
import com.arjuna.databroker.data.DataStore;
import com.arjuna.databroker.data.jee.annotation.DataConsumerInjection;
import com.arjuna.databroker.data.jee.annotation.DataProviderInjection;
import com.arjuna.databroker.data.jee.annotation.PostConfig;
import com.arjuna.databroker.data.jee.annotation.PostCreated;
import com.arjuna.databroker.data.jee.annotation.PostRecovery;

public class GlacierDataStore implements DataStore
{
    private static final Logger logger = Logger.getLogger(GlacierDataStore.class.getName());

    public static final String AWS_ACCESSKEYID_PROPERTYNAME     = "AWS Access Key ID";
    public static final String AWS_SECRETACCESSKEY_PROPERTYNAME = "AWS Secret Access Key";
    public static final String AWS_ACCOUNTID_PROPERTYNAME       = "AWS Account Id";
    public static final String GLACIER_ENDPOINTURL_PROPERTYNAME = "Glacier Endpoint URL";
    public static final String GLACIER_VAULTNAME_PROPERTYNAME   = "Glacier Vault Name";

    public GlacierDataStore(String name, Map<String, String> properties)
    {
        logger.log(Level.FINE, "GlacierDataStore: " + name + ", " + properties);

        _name       = name;
        _properties = properties;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public void setName(String name)
    {
        _name = name;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return Collections.unmodifiableMap(_properties);
    }

    @Override
    public void setProperties(Map<String, String> properties)
    {
        _properties = properties;
    }
    
    @Override
    public DataFlow getDataFlow()
    {
        return _dataFlow;
    }

    @Override
    public void setDataFlow(DataFlow dataFlow)
    {
        _dataFlow = dataFlow;
    }

    @PostConfig
    @PostCreated
    @PostRecovery
    public void setup()
    {
    }

    public void storeFile(File data)
    {
        logger.log(Level.FINE, "GlacierDataStore.store: data(file) = " + data);

        try
        {
            AWSCredentials      credentials = new BasicAWSCredentials(_properties.get(AWS_ACCESSKEYID_PROPERTYNAME), _properties.get(AWS_SECRETACCESSKEY_PROPERTYNAME));
            AmazonGlacierClient client      = new AmazonGlacierClient(credentials);
            client.setEndpoint(_properties.get(GLACIER_ENDPOINTURL_PROPERTYNAME));

            ArchiveTransferManager archiveTransferManager = new ArchiveTransferManager(client, credentials);
            archiveTransferManager.upload(_properties.get(GLACIER_VAULTNAME_PROPERTYNAME), "", data);
        }
        catch (Throwable throwable)
        {
            logger.log(Level.WARNING, "Unable to store to AmazonGlacier", throwable);
        }
    }

    public void storeBytes(byte[] data)
    {
        logger.log(Level.FINE, "GlacierDataStore.store: data(bytes) = " + data);

        try
        {
            AWSCredentials      credentials = new BasicAWSCredentials(_properties.get(AWS_ACCESSKEYID_PROPERTYNAME), _properties.get(AWS_SECRETACCESSKEY_PROPERTYNAME));
            AmazonGlacierClient client      = new AmazonGlacierClient(credentials);
            client.setEndpoint(_properties.get(GLACIER_ENDPOINTURL_PROPERTYNAME));

            UploadArchiveRequest uploadArchiveRequest = new UploadArchiveRequest();
            uploadArchiveRequest.withVaultName(_properties.get(GLACIER_VAULTNAME_PROPERTYNAME));
            uploadArchiveRequest.withChecksum(TreeHashGenerator.calculateTreeHash(Arrays.<byte[]>asList(data)));
            uploadArchiveRequest.withBody(new ByteArrayInputStream(data));
            uploadArchiveRequest.withContentLength((long) data.length);

            UploadArchiveResult uploadArchiveResult = client.uploadArchive(uploadArchiveRequest);
        }
        catch (Throwable throwable)
        {
            logger.log(Level.WARNING, "Unable to store to AmazonGlacier", throwable);
        }
    }

    @Override
    public Collection<Class<?>> getDataConsumerDataClasses()
    {
        Set<Class<?>> dataConsumerDataClasses = new HashSet<Class<?>>();

        dataConsumerDataClasses.add(File.class);
        
        return dataConsumerDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataConsumer<T> getDataConsumer(Class<T> dataClass)
    {
        if (File.class.isAssignableFrom(dataClass))
            return (DataConsumer<T>) _dataConsumerFile;
        else if (byte[].class.isAssignableFrom(dataClass))
            return (DataConsumer<T>) _dataConsumerBytes;
        else
            return null;
    }

    @Override
    public Collection<Class<?>> getDataProviderDataClasses()
    {
        Set<Class<?>> dataProviderDataClasses = new HashSet<Class<?>>();

        dataProviderDataClasses.add(String.class);
        dataProviderDataClasses.add(byte[].class);
        
        return dataProviderDataClasses;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> DataProvider<T> getDataProvider(Class<T> dataClass)
    {
        if (String.class.isAssignableFrom(dataClass))
            return (DataProvider<T>) _dataProvider;
        else
            return null;
    }

    private String               _name;
    private Map<String, String>  _properties;
    private DataFlow             _dataFlow;
    @DataConsumerInjection(methodName="storeFile")
    private DataConsumer<File>   _dataConsumerFile;
    @DataConsumerInjection(methodName="storeBytes")
    private DataConsumer<byte[]> _dataConsumerBytes;
    @DataProviderInjection
    private DataProvider<String> _dataProvider;
}
