package io.github.mike10004.containment.mavenplugin;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;

import java.util.List;

public interface DockerManager {

    DockerClient buildClient();

    default boolean queryImageExistsLocally(DockerClient client, String imageName) {
        return !queryImagesByName(client, imageName).isEmpty();
    }

    default boolean queryImageExistsLocally(String imageName) {
        return queryImageExistsLocally(buildClient(), imageName);
    }

    List<Image> queryImagesByName(DockerClient client, String imageName);

}
