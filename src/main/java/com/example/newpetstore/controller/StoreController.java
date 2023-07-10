package com.example.newpetstore.controller;

import com.example.newpetstore.config.JWTTokenProvider;
import com.example.newpetstore.entity.Order;
import com.example.newpetstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/store")
@SecurityRequirement(name = "token")
public class StoreController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Operation(description = "Add order (only for logged users)")
    @PostMapping("/order")
    public ResponseEntity<Order> save(@RequestBody Order order) {
        orderService.save(order);
        return ResponseEntity.ok(order);
    }

    @Operation(description = "Add pet to order (only for logged users)")
    @PutMapping("/order/{orderId}/add-pet")
    public ResponseEntity<Void> addPet(@PathVariable int orderId,
                                       int petId,
                                       HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUserUsernameFromJWT(token);

        if (orderService.addPet(orderId, petId, username)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(description = "Find order by id (only for logged and own users)")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> findById(@PathVariable int orderId) {

        Optional<Order> byId = orderService.findById(orderId);

        return byId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(description = "Delete order by id (only for logged and own users)")
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable int orderId,
                                       HttpServletRequest request) {

        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUserUsernameFromJWT(token);

        if (orderService.delete(orderId, username)) {

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

}
