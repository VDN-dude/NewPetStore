package com.example.newpetstore.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Pet {
    private Integer id;
    private Category category;
    private String name;
    private List<byte[]> photoUrls = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();
    private String status;
}
