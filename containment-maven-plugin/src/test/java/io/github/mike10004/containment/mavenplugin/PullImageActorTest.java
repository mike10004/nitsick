package io.github.mike10004.containment.mavenplugin;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class PullImageActorTest {

    private Random random;

    @Before
    public void setUp() {
        random = new Random("PullImageActorTest".hashCode());
    }

    @Test
    public void perform_pullHelloWorld_untagged() throws Exception {
        test_perform_pullHelloWorld(null);
    }

    @Test
    public void perform_pullHelloWorld_tag_linux() throws Exception {
        test_perform_pullHelloWorld("linux");
    }

    private void test_perform_pullHelloWorld(String tag) throws Exception {
        Tests.assumeDestructiveModeEnabled();
        String name = Uuids.randomUuidString(random);
        ImageSpecifier imageSpec = new ImageSpecifier("hello-world", tag);
        String remoteImageName = imageSpec.toString();
        DockerManager dockerManager = Tests.realDockerManager();
        Tests.enforceImageDoesNotExistLocally(dockerManager, imageSpec.withDefaultTag("latest").toString());
        RequireImageParametry parametry = RequireImageParametry.newBuilder(name).build();
        LogBucket log = new LogBucket();
        PullImageActor actor = new PullImageActor(log, dockerManager);
        actor.perform(parametry, remoteImageName);
        confirmTaggedImageExists(dockerManager, parametry.name);
    }

    private void confirmTaggedImageExists(DockerManager dockerManager, String name) {
        DockerClient client = dockerManager.buildClient();
        List<Image> images = dockerManager.queryImagesByName(client, name);
        if (images.isEmpty()) {
            images = client.listImagesCmd().withShowAll(true).exec();
            fail("image with name " + name + " not found among " + images);
        }
    }

    @Test
    public void resolveRemoteName() {
        String[][] testCases = {
                // localName, repositoryImageName, expected
                {"x", "y", "y"},
                {"x", null, "x"},
        };
        for (String[] testCase : testCases) {
            LogBucket log = new LogBucket();
            String name = testCase[0], repositoryImageName = testCase[1], expected = testCase[2];
            RequireImageParametry p = RequireImageParametry.newBuilder(name)
                    .build();
            String actual = new PullImageActor(log, Tests.mockDockerManager()).resolveRemoteName(p, repositoryImageName);
            assertEquals("name to pull from remote", expected, actual);
        }
    }

    @Test(expected = NullPointerException.class)
    public void resolveRemoteName_nullParametry() {
        new PullImageActor(new LogBucket(), Tests.mockDockerManager()).resolveRemoteName(null, "anything");
    }
}