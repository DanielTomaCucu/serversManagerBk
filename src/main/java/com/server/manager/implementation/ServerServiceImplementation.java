package com.server.manager.implementation;

import com.server.manager.enumeration.Status;
import com.server.manager.model.Server;
import com.server.manager.repository.ServerRepository;
import com.server.manager.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Random;

import static org.springframework.data.domain.PageRequest.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ServerServiceImplementation implements ServerService {
    private final ServerRepository serverRepository;

    @Override
    public Server create(Server server) {
        log.info("Saving new server" , server.getName());
        server.setImageUrl(setServerImageurl());
        return serverRepository.save(server);
    }



    @Override
    public Collection<Server> list(int limit) {
        log.info("Fetching all servers");
        return serverRepository.findAll(of(0, limit)).toList();
    }

    @Override
    public Server get(Long id) {
        log.info("Fetching server by ID" ,id);
        return serverRepository.findById(id).get();
    }

    @Override
    public Server update(Server server) {
        log.info("Update the server by id", server.getName());
        return serverRepository.save(server);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Delete server by id", id);
        serverRepository.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public Server ping(String ipAddress) throws IOException {
        log.info("Pinging server IP:" , ipAddress);
        Server server= serverRepository.findByIpAddress(ipAddress);
        InetAddress address = InetAddress.getByName(ipAddress);
        server.setStatus(address.isReachable(1000) ? Status.SERVER_UP:Status.SERVER_DOWN);
        return server;
    }

    private String setServerImageurl() {
        String[] imageNames={"server1.png","server2.png","server3.png","server4.png"};

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/server/image/"+
                        imageNames[new Random().nextInt(4)]).toUriString();
    }

}
