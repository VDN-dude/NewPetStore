package com.example.newpetstore.controller;

import com.example.newpetstore.entity.Order;
import com.example.newpetstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private OrderService orderService;

    @Operation(description = "Add order (only for logged users)")
    @PostMapping("/order")
    public ResponseEntity<Order> save(@RequestBody Order order,
                                      HttpSession httpSession){
            orderService.save(order);
            return ResponseEntity.ok(order);
    }

//    @Operation(description = "Add pet to order (only for logged users)")
//    @PutMapping("/order/{orderId}/add-pet")
//    public ResponseEntity<Void> addPet(@PathVariable int orderId,
//                                       int petId,
//                                       HttpSession httpSession){
//
//            if (orderService.addPet(orderId, petId)) {
//                return ResponseEntity.ok().build();
//            }
//        return ResponseEntity.notFound().build();
//    }
//
//    @Operation(description = "Find order by id (only for logged and own users)")
//    @GetMapping("/order/{orderId}")
//    public ResponseEntity<Order> findById(@PathVariable int orderId,
//                                          HttpSession httpSession){
//
//
//            Optional<Order> byId = orderService.findById(orderId);
//
//            if (byId.isPresent()) {
//
//                return ResponseEntity.ok(byId.get());
//        }
//
//        return ResponseEntity.notFound().build();
//    }
//
//    @Operation(description = "Delete order by id (only for logged and own users)")
//    @DeleteMapping("/order/{orderId}")
//    public ResponseEntity<Void> delete(@PathVariable int orderId,
//                                       HttpSession httpSession) {
//
//
//            if (orderService.delete(orderId)) {
//
//                return ResponseEntity.ok().build();
//            }
//
//        return ResponseEntity.badRequest().build();
//    }

}
