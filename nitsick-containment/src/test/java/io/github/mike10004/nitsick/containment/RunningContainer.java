package io.github.mike10004.nitsick.containment;

import java.util.List;

public interface RunningContainer extends AutoCloseable {

    String id();

    @Override
    void close() throws ContainmentException;

    List<PortMapping> fetchPorts() throws ContainmentException;

}
