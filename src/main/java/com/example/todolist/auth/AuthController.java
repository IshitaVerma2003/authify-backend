package com.example.todolist.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // REGISTER API
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        Map<String, String> response = new HashMap<>();

        try {
            User existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser != null) {
                response.put("message", "Email already registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            userRepository.save(user);

            String token = jwtUtil.generateToken(user.getEmail());

            response.put("token", token);
            response.put("message", "User Registered Successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Registration failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // LOGIN API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {

        Map<String, String> response = new HashMap<>();

        try {
            User existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser == null) {
                response.put("message", "Email is not registered");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!existingUser.getPassword().equals(user.getPassword())) {
                response.put("message", "Wrong password");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            String token = jwtUtil.generateToken(existingUser.getEmail());

            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Login error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}