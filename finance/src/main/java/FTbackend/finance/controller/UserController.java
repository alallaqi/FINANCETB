package FTbackend.finance.controller;

import FTbackend.finance.business.service.UserService;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String email = user.get("email");
        String password = user.get("password");

        try {
            User newUser = userService.registerNewUser(username, email, password);
            return ResponseEntity.ok(Map.of("id", newUser.getId(), "username", newUser.getUsername(), "email", newUser.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginDetails) {
        String username = loginDetails.get("username");
        String password = loginDetails.get("password");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            Map<String, Object> response = userService.loginUser(username, password);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        if (currentUser == null || !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("id", user.getId(), "username", user.getUsername(), "email", user.getEmail()));
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        if (currentUser == null || !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        String newUsername = updates.get("username");
        String newEmail = updates.get("email");

        try {
            User updatedUser = userService.updateUserProfile(id, newUsername, newEmail);
            return ResponseEntity.ok(Map.of("id", updatedUser.getId(), "username", updatedUser.getUsername(), "email", updatedUser.getEmail()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        if (currentUser == null || !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        try {
            userService.clearUserData(id);
            return ResponseEntity.ok("User data cleared");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}/calculations")
    public ResponseEntity<?> getUserCalculations(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        if (currentUser == null || !currentUser.getId().equals(id)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        Map<String, List<?>> calculations = userService.getAllUserCalculations(id);
        return ResponseEntity.ok(calculations);
    }
}