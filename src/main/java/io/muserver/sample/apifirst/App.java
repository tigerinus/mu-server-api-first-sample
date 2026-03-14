package io.muserver.sample.apifirst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;

import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import io.muserver.rest.RestHandlerBuilder;
import io.muserver.sample.apifirst.model.Pet;
import jakarta.ws.rs.core.Response;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Starting mu-server-api-first-sample app");

        PetStoreResource resource = new PetStoreResource();

        // Add some sample data
        resource.createPets(new Pet().id(1L).name("Fluffy").tag("cat"));
        resource.createPets(new Pet().id(2L).name("Buddy").tag("dog"));

        JacksonJsonProvider jsonProvider = new JacksonJsonProvider(new ObjectMapper());

        MuServer server = MuServerBuilder.muServer()
                .addHandler(
                        new RestHandlerBuilder()
                                .addCustomWriter(jsonProvider)
                                .addCustomReader(jsonProvider)
                                .addExceptionMapper(JacksonException.class, e -> {
                                    return Response.status(Response.Status.BAD_REQUEST)
                                            .entity("Invalid JSON: " + e.getMessage())
                                            .build();
                                })
                                .addResource(resource)
                                .build())
                .withHttpPort(8080)
                .start();

        log.info("Server started at " + server.uri() + "/pets");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down...");
            server.stop();
            log.info("Shut down complete.");
        }));
    }
}
