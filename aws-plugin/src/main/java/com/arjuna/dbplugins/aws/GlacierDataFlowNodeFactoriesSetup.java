/*
 * Copyright (c) 2015, Arjuna Technologies Limited, Newcastle-upon-Tyne, England. All rights reserved.
 */

package com.arjuna.dbplugins.aws;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import com.arjuna.databroker.data.DataFlowNodeFactory;
import com.arjuna.databroker.data.DataFlowNodeFactoryInventory;
import com.arjuna.dbplugins.aws.glacier.GlacierDataFlowNodeFactory;

@Startup
@Singleton
public class GlacierDataFlowNodeFactoriesSetup
{
    private static final Logger logger = Logger.getLogger(GlacierDataFlowNodeFactoriesSetup.class.getName());

    @PostConstruct
    public void setup()
    {
        logger.log(Level.FINE, "Setup : AmazonGlacier Data Flow Nodes Factory");

        DataFlowNodeFactory glacierDataFlowNodeFactory = new GlacierDataFlowNodeFactory("AmazonGlacier DataStore Factory", Collections.<String, String>emptyMap());

        _dataFlowNodeFactoryInventory.addDataFlowNodeFactory(glacierDataFlowNodeFactory);
    }

    @PreDestroy
    public void cleanup()
    {
        logger.log(Level.FINE, "Cleanup : AmazonGlacier Data Flow Nodes Factory");

        _dataFlowNodeFactoryInventory.removeDataFlowNodeFactory("AmazonGlacier DataStore Factory");
    }

    @EJB(lookup="java:global/databroker/data-core-jee/DataFlowNodeFactoryInventory")
    private DataFlowNodeFactoryInventory _dataFlowNodeFactoryInventory;
}
