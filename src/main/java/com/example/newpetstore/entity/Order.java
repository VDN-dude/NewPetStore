package com.example.newpetstore.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Order {
    private Integer id;
    private Integer petId;
    private Integer quantity;
    private String status;
    private boolean complete;
}
