package com.example.newpetstore.repository;


import com.example.newpetstore.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findAllByStatus(Pet.Status status);

}
