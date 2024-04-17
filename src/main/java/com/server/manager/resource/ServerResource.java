package com.server.manager.resource;

import com.server.manager.enumeration.Status;
import com.server.manager.implementation.ServerServiceImplementation;
import com.server.manager.model.Response;
import com.server.manager.model.Server;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.util.Map.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
    private final ServerServiceImplementation serverService;

    @GetMapping("/list")
    public ResponseEntity<Response> getServers(){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("servers", serverService.list(30)))
                        .message("Servers retrived")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server= serverService.ping(ipAddress);
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server",server))
                        .message(server.getStatus() == Status.SERVER_UP ? "Ping Success": "Ping Failed" )
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }
    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server){
        return  ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server", serverService.create(server)))
                        .message("Server was created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
        );
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("server",serverService.get(id)))
                        .message("Server retrieve")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()

        );
    }
    @DeleteMapping("/delete/{id}")
public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id){
        return ResponseEntity.ok(
                Response.builder()
                        .timeStamp(now())
                        .data(of("deleted", serverService.delete(id)))
                        .message("Server deleetd")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()

        );
    }
    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/Downloads/images/"+ fileName));
    }

}
