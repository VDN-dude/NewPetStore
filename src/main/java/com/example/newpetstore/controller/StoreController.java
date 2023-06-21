package com.example.newpetstore.controller;

import com.example.newpetstore.entity.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final List<Order> orders = new ArrayList<>();

    @PostMapping("/order")
    public ResponseEntity<Order> save(@RequestBody Order order){
        orders.add(order);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> findById(@PathVariable int orderId){
        for (Order order : orders) {

            if (order.getId() == orderId){

                return ResponseEntity.ok(order);
            }
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable int orderId) {

        int i = 0;

        for (Order order : orders) {

            if (order.getId() == orderId){

                orders.remove(i);
                return ResponseEntity.ok().build();
            }
            i++;
        }

        return ResponseEntity.notFound().build();
    }


//    @GetMapping("/inventory")
}
