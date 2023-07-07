package com.example.newpetstore.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "pet_store_pet")
@RequiredArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ElementCollection
    private List<byte[]> photo = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        AVAILABLE, PENDING, SOLD
    }
}
