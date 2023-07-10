package com.example.newpetstore.service;

import com.example.newpetstore.entity.Pet;
import com.example.newpetstore.entity.User;
import com.example.newpetstore.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public boolean update(int id, Pet pet, String username) {

        Optional<Pet> byId = petRepository.findById(id);

        if (byId.isPresent()) {

            Pet pet1 = byId.get();

            if (pet1.getUser().getUsername().equals(username)) {

                petRepository.save(pet);
                return true;
            }
        }
        return false;
    }

    public Optional<Pet> findById(int id) {
        return petRepository.findById(id);
    }

    public List<Pet> findByStatus(Pet.Status status) {
        return petRepository.findAllByStatus(status);
    }

    public boolean uploadImage(int id, byte[] imageBytes, String username) {

        Optional<Pet> byId = petRepository.findById(id);

        if (byId.isPresent()) {

            Pet pet = byId.get();

            if (pet.getUser().getUsername().equals(username)) {

                List<byte[]> photo = pet.getPhoto();
                photo.add(imageBytes);
                pet.setPhoto(photo);
                petRepository.save(pet);
                return true;
            }

        }
        return false;
    }

    public boolean delete(int id, String username) {
        Optional<Pet> byId = petRepository.findById(id);

        if (byId.isPresent()){

            Pet pet = byId.get();

            if (pet.getUser().getUsername().equals(username)){

                petRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
