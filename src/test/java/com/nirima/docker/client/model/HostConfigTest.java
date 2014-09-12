package com.nirima.docker.client.model;

import com.google.common.collect.Lists;
import junit.framework.TestCase;

import java.util.Collection;

public class HostConfigTest extends TestCase {

    public void testSetPortBindings() throws Exception {
        Iterable<PortMapping> mappings =
                PortMapping.parse("0.0.0.0:5000:5000/udp 127.0.0.1:22:5000/udp");

        HostConfig hostConfig = new HostConfig();
        hostConfig.setPortBindings(mappings);

        System.out.println(hostConfig.toString());
    }
}