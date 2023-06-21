package com.example.newpetstore.controller;

import com.example.newpetstore.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Map<String, User> users = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        users.put(user.getUsername(), user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/login")
    public ResponseEntity<User> login(String username,
                                      String password,
                                      HttpSession httpSession) {
        if (users.get(username) != null) {

            User user = users.get(username);

            if (user.getPassword().equals(password)) {

                httpSession.setAttribute("sessionUser", user);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession httpSession) {
        httpSession.removeAttribute("sessionUser");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUsername(@PathVariable String username) {

        if (users.get(username) != null) {

            return ResponseEntity.ok(users.get(username));
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{username}")
    public ResponseEntity<Void> update(@PathVariable String username,
                                           @RequestBody User user,
                                           HttpSession httpSession) {
        if (httpSession.getAttribute("sessionUser") != null) {

            User sessionUser = (User) httpSession.getAttribute("sessionUser");

            if (sessionUser.getUsername().equals(username)) {

                users.remove(username);
                users.put(user.getUsername(), user);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username,
                                           HttpSession httpSession) {

        if (httpSession.getAttribute("sessionUser") != null) {

            User sessionUser = (User) httpSession.getAttribute("sessionUser");

            if (sessionUser.getUsername().equals(username)) {

                users.remove(username);
                httpSession.removeAttribute("sessionUser");
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.badRequest().build();
    }

//    @PostMapping("/createWithList")


//    @PostMapping("/createWithArray")
}
