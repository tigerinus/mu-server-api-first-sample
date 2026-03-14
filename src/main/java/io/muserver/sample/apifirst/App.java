package io.muserver.sample.apifirst;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.muserver.MuServer;
import io.muserver.MuServerBuilder;
import io.muserver.rest.RestHandlerBuilder;
import io.muserver.sample.apifirst.model.Pet;

public class App {
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        log.info("Starting mu-server-api-first-sample app");

        PetStoreResource resource = new PetStoreResource();

        // Add some sample data
        resource.createPets(new Pet().id(1L).name("Fluffy").tag("cat"));
        resource.createPets(new Pet().id(2L).name("Buddy").tag("dog"));

        MuServer server = MuServerBuilder.muServer()
                .addHandler(
                        new RestHandlerBuilder()
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
