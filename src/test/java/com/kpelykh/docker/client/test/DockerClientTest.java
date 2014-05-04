package com.kpelykh.docker.client.test;

import com.nirima.docker.client.DockerException;
import com.nirima.docker.client.model.*;


import com.nirima.docker.client.DockerClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasItem;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

/**
 * Unit test for DockerClient.
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class DockerClientTest extends Assert
{
    public static final Logger LOG = LoggerFactory.getLogger(DockerClientTest.class);

    private DockerClient dockerClient;

    private List<String> tmpImgs = new ArrayList<String>();
    private List<String> tmpContainers = new ArrayList<String>();

    @BeforeTest
    public void beforeTest() throws DockerException, IOException {
		String url = System.getProperty("docker.url", "http://172.16.0.16:4243");
        LOG.info("======================= BEFORETEST =======================");
        LOG.info("Connecting to Docker server at " + url);
        dockerClient = DockerClient.builder().withUrl(url).build();
        LOG.info("Creating image 'busybox'");

        InputStream inputStream = dockerClient.createPullCommand()
                    .image("busybox")
                    .execute();

        System.out.println(IOUtils.toString(inputStream));

        assertNotNull(dockerClient);
        LOG.info("======================= END OF BEFORETEST =======================\n\n");
    }

    @AfterTest
    public void afterTest() {
        LOG.info("======================= END OF AFTERTEST =======================");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        LOG.info(String.format("################################## STARTING %s ##################################", method.getName()));
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        for (String image : tmpImgs) {
            LOG.info("Cleaning up temporary image " + image);
            try {
                dockerClient.imagesApi().removeImage(image);
            } catch (Exception ignore) {}
        }

        for (String container : tmpContainers) {
            LOG.info("Cleaning up temporary container " + container);
            try {
                dockerClient.container(container).remove();
            } catch (Exception ignore) {}
        }
        LOG.info(String.format("################################## END OF %s ##################################\n", result.getName()));
    }

    /*
     * #########################
     * ## INFORMATION TESTS ##
     * #########################
    */

    @Test
    public void testDockerVersion() throws DockerException {
        Version version = dockerClient.miscApi().version();
        LOG.info(version.toString());

        assertTrue(version.getGoVersion().length() > 0);
        assertTrue(version.getVersion().length() > 0);

        assertEquals(StringUtils.split(version.getVersion(), ".").length, 3);

    }

    @Test
    public void testDockerInfo() throws DockerException {
        Info dockerInfo = dockerClient.miscApi().info();
        LOG.info(dockerInfo.toString());

        assertTrue(dockerInfo.toString().contains("containers"));
        assertTrue(dockerInfo.toString().contains("images"));
        assertTrue(dockerInfo.toString().contains("debug"));

        assertTrue(dockerInfo.getContainers() > 0);
        assertTrue(dockerInfo.getImages() > 0);
        assertTrue(dockerInfo.getNFd() > 0);
        assertTrue(dockerInfo.getNGoroutines() > 0);
        assertTrue(dockerInfo.isMemoryLimit());
    }

    @Test
    public void testDockerSearch() throws DockerException {
        List<SearchItem> dockerSearch = dockerClient.images().search("busybox");
        LOG.info("Search returned {}", dockerSearch.toString());

        Matcher matcher = hasItem(hasField("name", equalTo("busybox")));
        assertThat(dockerSearch, matcher);

        assertThat(filter(hasField("name", is("busybox")), dockerSearch).size(), equalTo(1));
    }

    /*
     * ###################
     * ## LISTING TESTS ##
     * ###################
     */


    @Test
    public void testImages() throws DockerException {
        //List<Image> images = dockerClient.getImages(false);
        List<Image> images = dockerClient.images().finder().allImages(false).list();
        assertThat(images, notNullValue());
        LOG.info("Images List: {}", images);
        Info info = dockerClient.system().info();

        // Not true?
        // assertThat(images.size(), equalTo(info.getImages()));

        Image img = images.get(0);
        assertThat(img.getCreated(), is(greaterThan(0L)) );
        assertThat(img.getVirtualSize(), is(greaterThan(0L)) );
        assertThat(img.getId(), not(isEmptyString()));
        assertThat(img.getTag(), not(isEmptyString()));
        assertThat(img.getRepository(), not(isEmptyString()));
    }


    @Test
    public void testListContainers() throws DockerException {
        List<Container> containers = dockerClient.containers()
                                                 .finder()
                                                    .allContainers(true)
                                                    .list();
        assertThat(containers, notNullValue());
        LOG.info("Container List: {}", containers);

        int size = containers.size();

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[]{"echo"});

        ContainerCreateResponse container1 = dockerClient.containers().create(containerConfig);
        assertThat(container1.getId(), not(isEmptyString()));
        dockerClient.container(container1.getId()).start();
        tmpContainers.add(container1.getId());

        List containers2 = dockerClient.containers()
                .finder()
                .allContainers(true)
                .list();
        assertThat(size + 1, is(equalTo(containers2.size())));
        Matcher matcher = hasItem(hasField("id", startsWith(container1.getId())));
        assertThat(containers2, matcher);

        List<Container> filteredContainers = filter(hasField("id", startsWith(container1.getId())), containers2);
        assertThat(filteredContainers.size(), is(equalTo(1)));

        Container container2 = filteredContainers.get(0);
        assertThat(container2.getCommand(), not(isEmptyString()));
        assertThat(container2.getImage(), equalTo("busybox:latest"));
    }


    /*
     * #####################
     * ## CONTAINER TESTS ##
     * #####################
     */

    @Test
    public void testCreateContainer() throws DockerException {
        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[]{"true"});


        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        tmpContainers.add(container.getId());
    }

    @Test
    public void testStartContainer() throws DockerException {

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[]{"true"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        boolean add = tmpContainers.add(container.getId());

        dockerClient.container(container.getId()).start();

        ContainerInspectResponse containerInspectResponse = dockerClient.container(container.getId()).inspect();
        LOG.info("Container Inspect: {}", containerInspectResponse.toString());

        assertThat(containerInspectResponse.config, is(notNullValue()));
        assertThat(containerInspectResponse.getId(), not(isEmptyString()));

        assertThat(containerInspectResponse.getId(), startsWith(container.getId()));

        assertThat(containerInspectResponse.getImage(), not(isEmptyString()));
        assertThat(containerInspectResponse.getState(), is(notNullValue()));

        // Doesn't make sense in light of the code after this!
        //assertThat(containerInspectResponse.getState().running, is(true));

        if (!containerInspectResponse.getState().running) {
            assertThat(containerInspectResponse.getState().exitCode, is(equalTo(0)));
        }

    }

    @Test
    public void testWaitContainer() throws DockerException {

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[]{"true"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        tmpContainers.add(container.getId());

        dockerClient.container(container.getId()).start();

        int exitCode = dockerClient.container(container.getId()).waitForContainer();
        LOG.info("Container exit code: {}", exitCode);

        assertThat(exitCode, equalTo(0));

        ContainerInspectResponse containerInspectResponse = dockerClient.container(container.getId()).inspect();
        LOG.info("Container Inspect: {}", containerInspectResponse.toString());

        assertThat(containerInspectResponse.getState().running, is(equalTo(false)));
        assertThat(containerInspectResponse.getState().exitCode, is(equalTo(exitCode)));

    }

    @Test
    public void testLogs() throws DockerException, IOException {

        String snippet = "hello world";

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"/bin/echo", snippet});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.container(container.getId()).start();
        tmpContainers.add(container.getId());

        int exitCode = dockerClient.container(container.getId()).waitForContainer();

        assertThat(exitCode, equalTo(0));

        InputStream response = dockerClient.container(container.getId()).log();

        StringWriter logwriter = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line + (itr.hasNext() ? "\n" : ""));
                LOG.info(line);
            }
        } finally {
            IOUtils.closeQuietly(response);
        }

        String fullLog = logwriter.toString();

        LOG.info("Container log: {}", fullLog);
        assertThat(fullLog, equalTo(snippet));
    }

    //This test doesn't work in Ubuntu 12.04 due to
    //Error mounting '/dev/mapper/docker-8:5-...
    //ref: https://github.com/dotcloud/docker/issues/4036

    @Test
    public void testDiff() throws DockerException {
        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"touch", "/test"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.container(container.getId()).start();
        boolean add = tmpContainers.add(container.getId());
        int exitCode = dockerClient.container(container.getId()).waitForContainer();
        assertThat(exitCode, equalTo(0));

        List filesystemDiff = dockerClient.container(container.getId()).getFilesystemChanges();
        LOG.info("Container DIFF: {}", filesystemDiff.toString());

        assertThat(filesystemDiff.size(), greaterThan(1));
        FileChanges testChangeLog = selectUnique(filesystemDiff, hasField("path", equalTo("/test")));

        assertThat(testChangeLog, hasField("path", equalTo("/test")));
        assertThat(testChangeLog, hasField("kind", equalTo(1)));
    }

    @Test
    public void testStopContainer() throws DockerException {

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"sleep", "9999"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.container(container.getId()).start();
        tmpContainers.add(container.getId());

        LOG.info("Stopping container: {}", container.getId());
        dockerClient.container(container.getId()).stop(2);

        ContainerInspectResponse containerInspectResponse = dockerClient.container(container.getId()).inspect();
        LOG.info("Container Inspect: {}", containerInspectResponse.toString());

        assertThat(containerInspectResponse.getState().running, is(equalTo(false)));
        assertThat(containerInspectResponse.getState().exitCode, not(equalTo(0)));
    }

    @Test
    public void testKillContainer() throws DockerException {

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"sleep", "9999"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.container(container.getId()).start(); 
        tmpContainers.add(container.getId());

        LOG.info("Killing container: {}", container.getId());
        dockerClient.container(container.getId()).kill();

        ContainerInspectResponse containerInspectResponse = dockerClient.container(container.getId()).inspect();
        LOG.info("Container Inspect: {}", containerInspectResponse.toString());

        assertThat(containerInspectResponse.getState().running, is(equalTo(false)));
        assertThat(containerInspectResponse.getState().exitCode, not(equalTo(0)));

    }

    @Test
    public void restartContainer() throws DockerException {

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"sleep", "9999"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.container(container.getId()).start(); 
        tmpContainers.add(container.getId());

        ContainerInspectResponse containerInspectResponse = dockerClient.container(container.getId()).inspect();
        LOG.info("Container Inspect: {}",  containerInspectResponse.toString());

        String startTime = containerInspectResponse.getState().startedAt;

        dockerClient.container(container.getId()).restart(2);

        ContainerInspectResponse containerInspectResponse2 = dockerClient.container(container.getId()).inspect();
        LOG.info("Container Inspect After Restart: {}", containerInspectResponse2.toString());

        String startTime2 = containerInspectResponse2.getState().startedAt;

        assertThat(startTime, not(equalTo(startTime2)));

        assertThat(containerInspectResponse.getState().running, is(equalTo(true)));

        dockerClient.container(container.getId()).kill();
    }

    @Test
    public void removeContainer() throws DockerException {

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"true"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);

        dockerClient.container(container.getId()).start(); 
        dockerClient.container(container.getId()).waitForContainer();
        tmpContainers.add(container.getId());

        LOG.info("Removing container: {}", container.getId());
        dockerClient.container(container.getId()).remove();

        List containers2 = dockerClient.containers().finder().allContainers(true).list();
        Matcher matcher = not(hasItem(hasField("id", startsWith(container.getId()))));
        assertThat(containers2, matcher);

    }

    /*
     * ##################
     * ## IMAGES TESTS ##
     * ##################
     * */

    @Test
    public void testPullImage() throws DockerException, IOException {

        String testImage = "centos";

        LOG.info("Removing image: {}", testImage);
        dockerClient.image(testImage).remove();

        Info info = dockerClient.system().info();
        LOG.info("Client info: {}", info.toString());

        int imgCount= info.getImages();

        LOG.info("Pulling image: {}", testImage);
        // http://172.16.0.16:4243/v1.8/images/create?tag=&registry=&fromImage=centos
        InputStream response = dockerClient.createPullCommand().image(testImage).execute();

        StringWriter logwriter = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(response, "UTF-8");
            while (itr.hasNext()) {
                String line = itr.next();
                logwriter.write(line + "\n");
                LOG.info(line);
            }
        } finally {
            IOUtils.closeQuietly(response);
        }

        String fullLog = logwriter.toString();
        assertThat(fullLog, containsString("Download complete"));

        tmpImgs.add(testImage);

        info = dockerClient.system().info();
        LOG.info("Client info after pull, {}", info.toString());

        assertThat(imgCount, lessThan(info.getImages()));

        ImageInspectResponse imageInspectResponse = dockerClient.image(testImage).inspect();
        LOG.info("Image Inspect: {}", imageInspectResponse.toString());
        assertThat(imageInspectResponse, notNullValue());
    }

    //This test doesn't work in Ubuntu 12.04 due to
    //Error mounting '/dev/mapper/docker-8:5-...
    //ref: https://github.com/dotcloud/docker/issues/4036

    @Test
    public void commitImage() throws DockerException {

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"touch", "/test"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.container(container.getId()).start();
        tmpContainers.add(container.getId());

        LOG.info("Commiting container: {}", container.toString());
        String imageId = dockerClient.container(container.getId()).createCommitCommand()
                                                                  .execute();
        tmpImgs.add(imageId);

        ImageInspectResponse imageInspectResponse = dockerClient.image(imageId).inspect();
        LOG.info("Image Inspect: {}", imageInspectResponse.toString());

        assertThat(imageInspectResponse, hasField("container", startsWith(container.getId())));
        assertThat(imageInspectResponse.getContainerConfig().getImage(), equalTo("busybox"));

        ImageInspectResponse busyboxImg = dockerClient.image("busybox").inspect();

        assertThat(imageInspectResponse.getParent(), equalTo(busyboxImg.getId()));
    }

    @Test
    public void testRemoveImage() throws DockerException {


        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage("busybox");
        containerConfig.setCmd(new String[] {"touch", "/test"});

        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.container(container.getId()).start();
        tmpContainers.add(container.getId());

        LOG.info("Commiting container {}", container.toString());
        String imageId = dockerClient.container(container.getId()).createCommitCommand()
                                                                  .execute();

        tmpImgs.add(imageId);

        LOG.info("Removing image" + imageId);
        dockerClient.image(imageId).remove();

        List containers = dockerClient.containers()
                                      .finder().allContainers(true).list();
        
        Matcher matcher = not(hasItem(hasField("id", startsWith(imageId))));
        assertThat(containers, matcher);
    }


    /*
     *
     * ################
     * ## MISC TESTS ##
     * ################
     */

    @Test
    public void testRunShlex() throws DockerException {

        String[] commands = new String[] {
                "true",
                "echo \"The Young Descendant of Tepes & Septette for the Dead Princess\"",
                "echo -n 'The Young Descendant of Tepes & Septette for the Dead Princess'",
                "/bin/sh -c echo Hello World",
                "/bin/sh -c echo 'Hello World'",
                "echo 'Night of Nights'",
                "true && echo 'Night of Nights'"
        };

        for (String command : commands) {
            LOG.info("Running command: [{}]",  command);

            ContainerConfig containerConfig = new ContainerConfig();
            containerConfig.setImage("busybox");
            containerConfig.setCmd( commands );

            ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
            dockerClient.container(container.getId()).start(); 
            tmpContainers.add(container.getId());
            int exitcode = dockerClient.container(container.getId()).waitForContainer();
            assertThat(exitcode, equalTo(0));
        }
    }


    @Test
    public void testNginxDockerfileBuilder() throws DockerException, IOException {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("nginx").getFile());

        Collection<EventStreamItem> response = dockerClient.system().build(baseDir, null);

        boolean success = false;
        String imageId = null;
        for(EventStreamItem item : response ) {
            if( item.getStream().contains("Successfully built")) {
                success = true;
                imageId = StringUtils.substringAfterLast(item.getStream(), "Successfully built ").trim();
            }
        }

        assertThat(success, equalTo(true));

        ImageInspectResponse imageInspectResponse = dockerClient.image(imageId).inspect();
        assertThat(imageInspectResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", imageInspectResponse.toString());
        tmpImgs.add(imageInspectResponse.getId());

        assertThat(imageInspectResponse.getAuthor(), equalTo("Guillaume J. Charmes \"guillaume@dotcloud.com\""));
    }

    @Test
    public void testDockerBuilderAddFile() throws DockerException, IOException {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddFile").getFile());
        dockerfileBuild(baseDir, "Successfully executed testrun.sh");
    }

    @Test
    public void testDockerBuilderAddFolder() throws DockerException, IOException {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("testAddFolder").getFile());
        dockerfileBuild(baseDir, "Successfully executed testAddFolder.sh");
    }

    /*
    @Test

    public void testImportImageFromTar() throws DockerException, IOException {
        InputStream tar = Thread.currentThread().getContextClassLoader().getResourceAsStream("testImportImageFromTar/empty.tar");
        String imageId = dockerClient.importImage("empty", null, tar).getId();
        assert imageId.contains(dockerClient.inspectImage("empty").getId());
    }
     */

    @Test
    public void testNetCatDockerfileBuilder() throws DockerException, IOException, InterruptedException {
        File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("netcat").getFile());

        Collection<EventStreamItem> response = dockerClient.system().build(baseDir, null);

        boolean success = false;
        String imageId = null;
        for(EventStreamItem item : response ) {
            if( item.getStream().contains("Successfully built")) {
                success = true;
                imageId = StringUtils.substringAfterLast(item.getStream(), "Successfully built ").trim();
            }
        }

        assertThat(success, equalTo(true));

        ImageInspectResponse imageInspectResponse = dockerClient.image(imageId).inspect();
        assertThat(imageInspectResponse, not(nullValue()));
        LOG.info("Image Inspect: {}", imageInspectResponse.toString());
        tmpImgs.add(imageInspectResponse.getId());

        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage(imageInspectResponse.getId());
        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.container(container.getId()).start(); 
        tmpContainers.add(container.getId());

        ContainerInspectResponse containerInspectResponse = dockerClient.container(container.getId()).inspect();

        assertThat(containerInspectResponse.getId(), notNullValue());
        assertThat(containerInspectResponse.getNetworkSettings().ports, notNullValue());

        //No use as such if not running on the server
        for(String portstr : containerInspectResponse.getNetworkSettings().ports.getAllPorts().keySet()){

         Ports.Port p = containerInspectResponse.getNetworkSettings().ports.getAllPorts().get(portstr);
         int port = Integer.valueOf(p.getHostPort());
        LOG.info("Checking port {} is open", port);
        assertThat(available(port), is(false));
        }


        dockerClient.container(container.getId()).stop(0);


    }


    // UTIL

    /**
     * Checks to see if a specific port is available.
     *
     * @param port the port to check for availability
     */
    public static boolean available(int port) {
        if (port < 1100 || port > 60000) {
            throw new IllegalArgumentException("Invalid start port: " + port);
        }

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }

    private void dockerfileBuild(File baseDir, String expectedText) throws DockerException, IOException {

        //Build image
        Collection<EventStreamItem> response = dockerClient.system().build(baseDir, null);

        boolean success = false;
        String imageId = null;
        for(EventStreamItem item : response ) {
            LOG.info("Response: {}", item.toString());
            if( item.getStream().contains("Successfully built")) {
                success = true;
                imageId = StringUtils.substringAfterLast(item.getStream(), "Successfully built ").trim();
            }
        }

        assertThat(success, equalTo(true));

        //Create container based on image
        ContainerConfig containerConfig = new ContainerConfig();
        containerConfig.setImage(imageId);
        ContainerCreateResponse container = dockerClient.containers().create(containerConfig);
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.container(container.getId()).start(); 
        dockerClient.container(container.getId()).waitForContainer();

        tmpContainers.add(container.getId());

        //Log container
        InputStream logResponse = dockerClient.container(container.getId()).log();

        StringWriter logwriter2 = new StringWriter();

        try {
            LineIterator itr = IOUtils.lineIterator(logResponse, "UTF-8");
            while (itr.hasNext()) {
                String line = itr.next();
                logwriter2.write(line + (itr.hasNext() ? "\n" : ""));
                LOG.info(line);
            }
        } finally {
            IOUtils.closeQuietly(logResponse);
        }

        assertThat(logwriter2.toString(), endsWith(expectedText));
    }
}