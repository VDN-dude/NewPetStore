package com.example.newpetstore.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Tag {
    private Integer id;
    private String name;
}
