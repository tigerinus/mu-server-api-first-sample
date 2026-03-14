package io.muserver.sample.apifirst;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.ArrayList;

import io.muserver.sample.apifirst.api.PetsApi;
import io.muserver.sample.apifirst.model.Pet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;

public class PetStoreResource implements PetsApi {
    private final Map<Long, Pet> petstore = new ConcurrentHashMap<>();

    @Override
    public Response createPets(@Valid @NotNull Pet pet) {
        if (pet == null || pet.getId() == null) {
            return Response.status(400).build();
        }
        petstore.put(pet.getId(), pet);
        return Response.status(201).build();
    }

    @Override
    public Response listPets(@Max(100) Integer limit) {
        List<Pet> list = new ArrayList<>(petstore.values());
        if (limit != null && limit > 0 && limit < list.size()) {
            list = list.subList(0, Math.min(limit, list.size()));
        }
        return Response.ok(list).build();
    }

    @Override
    public Response showPetById(String petId) {
        if (petId == null) {
            return Response.status(404).build();
        }
        try {
            long id = Long.parseLong(petId);
            Pet pet = petstore.get(id);
            if (pet == null) {
                return Response.status(404).build();
            }
            return Response.ok(pet).build();
        } catch (NumberFormatException e) {
            return Response.status(404).build();
        }
    }

}
