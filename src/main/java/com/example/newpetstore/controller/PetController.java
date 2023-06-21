package com.example.newpetstore.controller;

import com.example.newpetstore.entity.Pet;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pet")
public class PetController {

    private final List<Pet> pets = new ArrayList<>();
    private final String[] statuses = new String[]{"available", "pending", "sold"};

    @PostMapping
    public ResponseEntity<Pet> save(@RequestBody Pet pet) {
        pets.add(pet);
        return ResponseEntity.ok(pet);
    }

    @PutMapping
    public ResponseEntity<Pet> update(int id,
                                      @RequestBody Pet pet) {
        if (pets.get(id) != null) {

            pets.add(pet);
            return ResponseEntity.ok(pet);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{petId}")
    public ResponseEntity<Pet> findById(@PathVariable int petId) {

        for (Pet pet : pets) {

            if (pet.getId() == petId) {

                return ResponseEntity.ok(pet);
            }

        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{petId}")
    public ResponseEntity<Pet> updateWithForm(@PathVariable int petId,
                                              String name,
                                              String status) {
        for (Pet pet : pets) {

            if (pet.getId() == petId){

                pet.setName(name);
                pet.setStatus(status);

                return ResponseEntity.ok(pet);
            }

        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> delete(@PathVariable int petId) {

        int i = 0;

        for (Pet pet : pets) {

            if (pet.getId() == petId){

                pets.remove(i);
                return ResponseEntity.ok().build();
            }
            i++;
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<List<Pet>> findByStatus(String status){
        List<Pet> petList = new ArrayList<>();
        for (String s : statuses) {

            if (!s.equals(status)) {

                for (Pet pet : pets) {

                    if (pet.getStatus().equals(s)){
                        petList.add(pet);
                    }
                }
            }
        }
        return ResponseEntity.ok(petList);
    }

    @SneakyThrows
    @PostMapping("/{petId}/uploadImage")
    public ResponseEntity<Pet> uploadImage(@PathVariable int petId,
                                           MultipartFile file){

        for (Pet pet : pets) {

            if (pet.getId() == petId){

                byte[] bytes = file.getBytes();
                List<byte[]> photoUrls = pet.getPhotoUrls();
                photoUrls.add(bytes);
                pet.setPhotoUrls(photoUrls);

                return ResponseEntity.ok(pet);
            }
        }

        return ResponseEntity.notFound().build();
    }
}
