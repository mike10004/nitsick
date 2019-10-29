package io.github.mike10004.containment.mavenplugin;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.google.common.collect.ImmutableMap;
import org.junit.rules.ExternalResource;

import java.util.List;

public class TestCleanupRule extends ExternalResource {

    public static final String INCLUDE_LABEL_NAME = "containment-maven-plugin-tests-include";
    public static final String INCLUDE_LABEL_VALUE = "true";

    @Override
    protected void after() {
        if (!Tests.isRealDockerManagerAnyClientsCreated()) {
            return;
        }
        DockerClient client = Tests.realDockerManager().buildClient();
        List<Image> images = client.listImagesCmd().withLabelFilter(ImmutableMap.of(INCLUDE_LABEL_NAME, INCLUDE_LABEL_VALUE)).exec();
        System.out.format("%d images found with label %s=%s%n", images.size(), INCLUDE_LABEL_NAME, INCLUDE_LABEL_VALUE);
        for (Image image : images) {
            System.out.format("removing image %s (created %s)%n", image.getId(), image.getCreated());
            client.removeImageCmd(image.getId()).withForce(true).exec();
            System.out.format("removed image %s%n", image.getId());
        }
    }

}
