package com.server.manager.service;

import com.server.manager.model.Server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Collections;

public interface ServerService {
    Server create(Server server);
    Collection<Server> list(int limit);
    Server get(Long id);
    Server update(Server server);
    Boolean delete(Long id);
    Server ping(String ipAddress) throws IOException;
}
