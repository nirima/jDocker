package com.nirima.docker.client.model;

import junit.framework.TestCase;

public class PortMappingTest extends TestCase {

    public void testFromString() throws Exception {
        // ip:hostPort:containerPort |
        // ip::containerPort
        // hostPort:containerPort
        // containerPort

        PortMapping.fromString("0.0.0.0:123:33");
        assertTrue(PortMapping.fromString("0.0.0.0::33").hostPort == null);
        PortMapping.fromString("33:33");
        PortMapping.fromString("33");

    }
}