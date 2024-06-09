package FTbackend.finance.controller;

import FTbackend.finance.business.service.UserService;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.domain.Calculation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam Set<String> roles) {
        User registeredUser = userService.registerNewUser(username, email, password, roles);
        Map<String, Object> response = new HashMap<>();
        response.put("id", registeredUser.getId());
        response.put("username", registeredUser.getUsername());
        response.put("email", registeredUser.getEmail());
        response.put("roles", registeredUser.getRoles());
        return ResponseEntity.ok(response);
    }



    @PostMapping("/login")
    @Operation(summary = "Login a user")
    public ResponseEntity<?> loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            Map<String, Object> response = userService.loginUser(username, password);
            System.out.println("Login response: " + response);  // Debug log
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());  // Debug log
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Login failed", "message", e.getMessage()));
        }
    }

    @GetMapping("/profile/{id}")
    @Operation(summary = "Get user profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            List<Calculation> calculations = userService.getUserCalculations(id);
            if (user != null) {
                Map<String, Object> result = Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "calculations", calculations
                );
                System.out.println("Profile fetched: " + result);
                return ResponseEntity.ok(result);
            } else {
                System.out.println("User not found for ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving user profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/admin/all-users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/admin/clear-user-data/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Clear all data from a user profile (Admin only)")
    public ResponseEntity<String> clearUserData(@PathVariable Long id) {
        try {
            userService.clearUserData(id);
            return ResponseEntity.ok("User data cleared successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body("User not found.");
        }
    }
}
