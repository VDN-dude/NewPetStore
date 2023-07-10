package com.example.newpetstore.controller;

import com.example.newpetstore.config.JWTTokenProvider;
import com.example.newpetstore.entity.Pet;
import com.example.newpetstore.entity.User;
import com.example.newpetstore.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pet")
@SecurityRequirement(name = "token")
public class PetController {
    @Autowired
    private PetService petService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Operation(summary = "Add pet in the store", description = "only for logged users")
    @PostMapping
    public ResponseEntity<Pet> save(@RequestBody Pet pet) {
        petService.save(pet);
        return ResponseEntity.ok(pet);
    }

    @Operation(summary = "Update pet", description = "only for logged users")
    @PutMapping
    public ResponseEntity<Pet> update(int id, @RequestBody Pet pet,
                                      HttpServletRequest request) {

        Optional<User> authenticationUser = getAuthenticationUser(request);

        if (authenticationUser.isPresent()) {
            if (petService.update(id, pet, authenticationUser.get())) {

                return ResponseEntity.ok(pet);
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Find pet by id")
    @GetMapping("/{petId}")
    public ResponseEntity<Pet> findById(@PathVariable int petId) {

        Optional<Pet> byId = petService.findById(petId);

        return byId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete pet by id", description = "only for logged and own users")
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> delete(@PathVariable int petId,
                                       HttpServletRequest request) {


        Optional<User> authenticationUser = getAuthenticationUser(request);

        if (authenticationUser.isPresent()) {
            if (petService.delete(petId, authenticationUser.get())) {

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/find-by-status")
    public ResponseEntity<List<Pet>> findByStatus(Pet.Status status) {
        List<Pet> byStatus = petService.findByStatus(status);
        return ResponseEntity.ok(byStatus);
    }

    @SneakyThrows
    @Operation(summary = "Upload image to pet", description = "only for logged and own users")
    @PostMapping("/{petId}/upload-image")
    public ResponseEntity<Pet> uploadImage(@PathVariable int petId,
                                           MultipartFile file,
                                           HttpServletRequest request) {

        Optional<User> authenticationUser = getAuthenticationUser(request);

        if (authenticationUser.isPresent()) {

            if (petService.uploadImage(petId, file.getBytes(), authenticationUser.get())) {

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().build();
    }

    private Optional<User> getAuthenticationUser(HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);
        if (jwtTokenProvider.validateToken(token)) {

            return Optional.ofNullable((User) jwtTokenProvider.getAuthentication(token));
        }
        return Optional.empty();
    }
}
