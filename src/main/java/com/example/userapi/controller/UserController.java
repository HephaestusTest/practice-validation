package com.example.userapi.controller;

import com.example.userapi.model.User;
import com.example.userapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers(@RequestParam(defaultValue = "0") int page) {
        List<User> all = userService.findAll();
        int from = Math.min(page * 25, all.size());
        int to = Math.min(from + 25, all.size());
        return ResponseEntity.ok(all.subList(from, to));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request.email(), request.displayName());
        return ResponseEntity.status(201).body(user);
    }

    public record CreateUserRequest(String email, String displayName) {}
}
