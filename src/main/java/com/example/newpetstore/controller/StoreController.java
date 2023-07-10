package com.example.newpetstore.controller;

import com.example.newpetstore.config.JWTTokenProvider;
import com.example.newpetstore.entity.Order;
import com.example.newpetstore.entity.User;
import com.example.newpetstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/store")
@SecurityRequirement(name = "token")
public class StoreController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Operation(summary = "Add order", description = "only for logged users")
    @PostMapping("/order")
    public ResponseEntity<Order> save(@RequestBody Order order) {
        orderService.save(order);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Add pet to order", description = "only for logged users")
    @PutMapping("/order/{orderId}/add-pet")
    public ResponseEntity<Void> addPet(@PathVariable int orderId,
                                       int petId,
                                       HttpServletRequest request) {


        Optional<User> authenticationUser = getAuthenticationUser(request);

        if (authenticationUser.isPresent()) {

            if (orderService.addPet(orderId, petId, authenticationUser.get())) {

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Find order by id", description = "only for logged and own users")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Order> findById(@PathVariable int orderId) {

        Optional<Order> byId = orderService.findById(orderId);

        return byId.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(summary = "Delete order by id", description = "only for logged and own users")
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> delete(@PathVariable int orderId,
                                       HttpServletRequest request) {

        Optional<User> authenticationUser = getAuthenticationUser(request);

        if (authenticationUser.isPresent()) {

            if (orderService.delete(orderId, authenticationUser.get())) {

                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
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
