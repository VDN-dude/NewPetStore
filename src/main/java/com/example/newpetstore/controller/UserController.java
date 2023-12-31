package com.example.newpetstore.controller;

import com.example.newpetstore.config.JWTTokenProvider;
import com.example.newpetstore.entity.User;
import com.example.newpetstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Operation(description = "Create new user")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.save(user);
        return ResponseEntity.ok(user);
    }

    @Operation(description = "Login in the store")
    @PostMapping("/login")
    public ResponseEntity<String> login(String username,
                                        String password) {

        Optional<User> user = userService.login(username, password);

        if (user.isPresent()) {

            String token = jwtTokenProvider.generateToken(username, user.get().getRoles());
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(description = "Logout from the store")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Find user by username")
    @GetMapping("/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {

        Optional<User> byUsername = userService.findByUsername(username);

        return byUsername.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(summary = "Update user", description = "only for logged users")
    @SecurityRequirement(name = "token")
    @PutMapping("/{username}")
    public ResponseEntity<Void> update(@PathVariable String username,
                                       @RequestBody User user,
                                       HttpServletRequest request) {

        Optional<User> authenticationUser = getAuthenticationUser(request);

        if (authenticationUser.isPresent()) {

            if (authenticationUser.get().getUsername().equals(username)) {

                userService.update(username, user);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Delete user by username", description = "only for logged user for own account")
    @SecurityRequirement(name = "token")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username,
                                       HttpServletRequest request) {

        Optional<User> authenticationUser = getAuthenticationUser(request);

        if (authenticationUser.isPresent()) {

            if (authenticationUser.get().getUsername().equals(username)) {

                userService.delete(username);
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
