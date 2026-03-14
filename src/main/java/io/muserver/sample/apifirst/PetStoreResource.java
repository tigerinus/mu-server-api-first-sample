package io.muserver.sample.apifirst;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.muserver.sample.apifirst.api.PetsApi;
import io.muserver.sample.apifirst.model.Pet;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

public class PetStoreResource implements PetsApi {
    private final Map<Long, Pet> petstore = new ConcurrentHashMap<>();

    @Override
    public void createPets(@Valid @NotNull Pet pet) {
        if (pet == null || pet.getId() == null) {
            throw new BadRequestException("Invalid pet data");
        }
        petstore.put(pet.getId(), pet);
    }

    @Override
    public List<Pet> listPets(@Max(100) Integer limit) {
        List<Pet> list = new ArrayList<>(petstore.values());
        if (limit != null && limit > 0 && limit < list.size()) {
            list = list.subList(0, Math.min(limit, list.size()));
        }
        return list;
    }

    @Override
    public Pet showPetById(String petId) {
        if (petId == null) {
            throw new BadRequestException("Invalid pet ID");
        }
        try {
            long id = Long.parseLong(petId);
            Pet pet = petstore.get(id);
            if (pet == null) {
                throw new NotFoundException("Pet not found");
            }
            return pet;
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid pet ID");
        }
    }

}
