package com.nirima.docker.client.model;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * -p style parameters.
 */
public class PortMapping {

    public final boolean isUDP;
    public final Integer hostPort;
    public final Integer containerPort;
    public final String ip;

    private PortMapping(boolean isUDP, Integer hostPort, Integer containerPort, String ip) {
        Preconditions.checkNotNull(containerPort);
        this.isUDP = isUDP;
        this.hostPort = hostPort;
        this.containerPort = containerPort;
        this.ip = ip;
    }

    public static PortMapping fromString(String mapping) {

        boolean isUDP = false;
        Integer hostPort = null;
        Integer containerPort = null;
        String ip = null;


        if( mapping.endsWith("/udp") ) {
            mapping = mapping.substring(0, mapping.length() - 4);
            isUDP = true;
        }

        String[] elements = mapping.split(":");

        //
        // ip:hostPort:containerPort |
        // ip::containerPort
        // hostPort:containerPort
        // containerPort

        if( elements.length == 1 ) {
            containerPort = Integer.parseInt(elements[0]);
        } else if( elements.length == 2 ) {
            hostPort = Integer.parseInt(elements[0]);
            containerPort = Integer.parseInt(elements[1]);
        } else if( elements.length == 3) {
            ip = elements[0];
            if( elements[1].length() > 0) {
                hostPort = Integer.parseInt(elements[1]);
            }
            containerPort = Integer.parseInt(elements[2]);
        }
        return new PortMapping(isUDP, hostPort, containerPort, ip);
    }

    public static Iterable<PortMapping> parse(String mappings) {
        Iterable<String> items = Splitter.on(" ").omitEmptyStrings().trimResults().split(mappings);

        return Iterables.transform(items, new Function<String, PortMapping>() {
            @Override
            public PortMapping apply(@Nullable String input) {
                return PortMapping.fromString(input);
            }
        });

    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("isUDP", isUDP)
                .add("hostPort", hostPort)
                .add("containerPort", containerPort)
                .add("ip", ip)
                .toString();
    }

    public String getContainerPortString() {
        return "" + containerPort + ((isUDP)?"/udp":"/tcp");
    }

    public PortBinding getHostPortBinding() {
        PortBinding portBinding = new PortBinding();
        if( ip != null )
            portBinding.hostIp = ip;
        if( hostPort != null )
            portBinding.hostPort = "" + hostPort;
        return portBinding;
    }
}
