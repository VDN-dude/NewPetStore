package com.example.newpetstore.controller;

import com.example.newpetstore.config.JWTTokenProvider;
import com.example.newpetstore.entity.Pet;
import com.example.newpetstore.entity.User;
import com.example.newpetstore.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Operation(description = "Add pet in the store (only for logged users)")
    @PostMapping
    public ResponseEntity<Pet> save(@RequestBody Pet pet) {
        petService.save(pet);
        return ResponseEntity.ok(pet);
    }

    @Operation(description = "Update pet (only for logged users)")
    @PutMapping
    public ResponseEntity<Pet> update(int id, @RequestBody Pet pet,
                                      HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUserUsernameFromJWT(token);

        if (petService.update(id, pet, username)) {

            return ResponseEntity.ok(pet);
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(description = "Find pet by id")
    @GetMapping("/{petId}")
    public ResponseEntity<Pet> findById(@PathVariable int petId) {

        Optional<Pet> byId = petService.findById(petId);

        if (byId.isPresent()) {

            return ResponseEntity.ok(byId.get());
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(description = "Delete pet by id (only for logged and own users)")
    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> delete(@PathVariable int petId,
                                       HttpServletRequest request) {


        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUserUsernameFromJWT(token);

        if (petService.delete(petId, username)) {

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/find-by-status")
    public ResponseEntity<List<Pet>> findByStatus(Pet.Status status) {
        List<Pet> byStatus = petService.findByStatus(status);
        return ResponseEntity.ok(byStatus);
    }

    @SneakyThrows
    @Operation(description = "Upload image to pet (only for logged and own users)")
    @PostMapping("/{petId}/upload-image")
    public ResponseEntity<Pet> uploadImage(@PathVariable int petId,
                                           MultipartFile file,
                                           HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUserUsernameFromJWT(token);

        if (petService.uploadImage(petId, file.getBytes(), username)) {

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
