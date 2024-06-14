package FTbackend.finance.controller;

import FTbackend.finance.business.service.MortgageService;
import FTbackend.finance.data.domain.Mortgage;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/mortgage")
public class MortgageController {

    private final MortgageService mortgageService;
    private final UserRepository userRepository;

    @Autowired
    public MortgageController(MortgageService mortgageService, UserRepository userRepository) {
        this.mortgageService = mortgageService;
        this.userRepository = userRepository;
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMortgage(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            double principal = Double.parseDouble(payload.get("principal").toString());
            double interestRate = Double.parseDouble(payload.get("interestRate").toString());
            int term = Integer.parseInt(payload.get("term").toString());

            double result = mortgageService.calculateMonthlyPayment(principal, interestRate, term);

            // Save the mortgage for the authenticated user
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
            Mortgage mortgage = new Mortgage();
            mortgage.setPrincipal(principal);
            mortgage.setInterestRate(interestRate);
            mortgage.setTerm(term);
            mortgage.setUser(user);
            mortgageService.saveMortgage(mortgage, user.getId());

            return ResponseEntity.ok(Map.of("mortgageResult", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred during the calculation.");
        }
    }
}