//package com.kpelykh.docker.client.test;
//
//import com.kpelykh.docker.client.DockerClient;
//import com.kpelykh.docker.client.DockerException;
//import com.kpelykh.docker.client.model.*;
//
//import junit.framework.TestCase;
//
//import java.io.PrintStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by magnayn on 09/01/2014.
// */
//public class DockerTemplateTest extends TestCase {
//    public void testUp() throws DockerException {
//        DockerClient dockerClient = new DockerClient("http://172.16.42.43:4243");
//
//
//        ContainerConfig containerConfig = new ContainerConfig();
//        containerConfig.setImage("jenkins-3");
//        containerConfig.setCmd(new String[]{"/usr/sbin/sshd", "-D"});
//        containerConfig.setPortSpecs(new String[]{"22"});
//        ExposedPort o = new ExposedPort();
//        Map<String, ExposedPort> eports = new HashMap<String, ExposedPort>();
//
//        eports.put("22/tcp",o);
//
//        containerConfig.setExposedPorts(eports);
//
//
//        System.out.println(containerConfig);
//
//
//        ContainerCreateResponse container = dockerClient.create(containerConfig);
//
//
//        Map<String, PortBinding[]> bports = new HashMap<String, PortBinding[]>();
//        PortBinding binding = new PortBinding();
//        binding.hostIp = "0.0.0.0";
//       // binding.hostPort = "";
//        bports.put("22/tcp", new PortBinding[] { binding });
//
//        HostConfig hostConfig = new HostConfig();
//        hostConfig.setPortBindings(bports);
//
//        dockerClient.startContainer(container.getId(), hostConfig);
//
//
//        String containerId = container.getId();
//
//        //HostConfig hostConfig = new HostConfig();
//
//        //dockerClient.startContainer(containerId);
//
//        ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainer(containerId);
//        System.out.println(containerInspectResponse);
//
//    }
//}
