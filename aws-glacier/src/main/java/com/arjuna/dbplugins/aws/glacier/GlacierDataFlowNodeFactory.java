/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.aws.glacier;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.arjuna.databroker.data.DataFlowNode;
import com.arjuna.databroker.data.DataFlowNodeFactory;
import com.arjuna.databroker.data.DataStore;
import com.arjuna.databroker.data.InvalidClassException;
import com.arjuna.databroker.data.InvalidMetaPropertyException;
import com.arjuna.databroker.data.InvalidNameException;
import com.arjuna.databroker.data.InvalidPropertyException;
import com.arjuna.databroker.data.MissingMetaPropertyException;
import com.arjuna.databroker.data.MissingPropertyException;

public class GlacierDataFlowNodeFactory implements DataFlowNodeFactory
{
    public GlacierDataFlowNodeFactory(String name, Map<String, String> properties)
    {
        _name       = name;
        _properties = properties;
    }

    @Override
    public String getName()
    {
        return _name;
    }

    @Override
    public Map<String, String> getProperties()
    {
        return _properties;
    }

    @Override
    public List<Class<? extends DataFlowNode>> getClasses()
    {
        List<Class<? extends DataFlowNode>> classes = new LinkedList<Class<? extends DataFlowNode>>();
        
        classes.add(DataStore.class);
        
        return classes;
    }

    @Override
    public <T extends DataFlowNode> List<String> getMetaPropertyNames(Class<T> dataFlowNodeClass)
        throws InvalidClassException
    {
        if (dataFlowNodeClass.isAssignableFrom(GlacierDataStore.class))
            return Collections.emptyList();
        else
            throw new InvalidClassException("Unsupported class", dataFlowNodeClass.getName());
    }

    @Override
    public <T extends DataFlowNode> List<String> getPropertyNames(Class<T> dataFlowNodeClass, Map<String, String> metaProperties)
        throws InvalidClassException, InvalidMetaPropertyException, MissingMetaPropertyException
    {
        if (dataFlowNodeClass.isAssignableFrom(GlacierDataStore.class))
        {
            if (metaProperties.isEmpty())
            {
                List<String> propertyNames = new LinkedList<String>();

                propertyNames.add(GlacierDataStore.AWS_ACCESSKEYID_PROPERTYNAME);
                propertyNames.add(GlacierDataStore.AWS_SECRETACCESSKEY_PROPERTYNAME);
                propertyNames.add(GlacierDataStore.AWS_ACCOUNTID_PROPERTYNAME);
                propertyNames.add(GlacierDataStore.GLACIER_ENDPOINTURL_PROPERTYNAME);
                propertyNames.add(GlacierDataStore.GLACIER_VAULTNAME_PROPERTYNAME);

                return propertyNames;
            }
            else
                throw new InvalidMetaPropertyException("No meta properties expected", null, null);
        }
        else
            throw new InvalidClassException("Unsupported class", dataFlowNodeClass.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DataFlowNode> T createDataFlowNode(String name, Class<T> dataFlowNodeClass, Map<String, String> metaProperties, Map<String, String> properties)
        throws InvalidNameException, InvalidClassException, InvalidMetaPropertyException, MissingMetaPropertyException, InvalidPropertyException, MissingPropertyException
    {
        if (dataFlowNodeClass.isAssignableFrom(GlacierDataStore.class))
        {
            if (metaProperties.isEmpty())
            {
                if (! properties.containsKey(GlacierDataStore.AWS_ACCESSKEYID_PROPERTYNAME))
                    throw new MissingPropertyException("Properties expected", GlacierDataStore.AWS_ACCESSKEYID_PROPERTYNAME);
                else if (! properties.containsKey(GlacierDataStore.AWS_SECRETACCESSKEY_PROPERTYNAME))
                    throw new MissingPropertyException("Properties expected", GlacierDataStore.AWS_SECRETACCESSKEY_PROPERTYNAME);
                else if (! properties.containsKey(GlacierDataStore.AWS_ACCOUNTID_PROPERTYNAME))
                    throw new MissingPropertyException("Properties expected", GlacierDataStore.AWS_ACCOUNTID_PROPERTYNAME);
                else if (! properties.containsKey(GlacierDataStore.GLACIER_ENDPOINTURL_PROPERTYNAME))
                    throw new MissingPropertyException("Properties expected", GlacierDataStore.GLACIER_ENDPOINTURL_PROPERTYNAME);
                else if (! properties.containsKey(GlacierDataStore.GLACIER_VAULTNAME_PROPERTYNAME))
                    throw new MissingPropertyException("Properties expected", GlacierDataStore.GLACIER_VAULTNAME_PROPERTYNAME);
                else if (properties.size() != 5)
                    throw new InvalidPropertyException("Unexpected properties", null, null);

                return (T) new GlacierDataStore(name, properties);
            }
            else
                throw new InvalidMetaPropertyException("No metaproperties expected", null, null);
        }
        else
            throw new InvalidClassException("Unsupported class", dataFlowNodeClass.getName());
    }

    private String              _name;
    private Map<String, String> _properties;
}
