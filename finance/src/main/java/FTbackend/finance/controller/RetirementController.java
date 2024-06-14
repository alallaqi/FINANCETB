package FTbackend.finance.controller;

import FTbackend.finance.business.service.RetirementService;
import FTbackend.finance.data.domain.RetirementPlan;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/retirement")
public class RetirementController {

    private final RetirementService retirementService;
    private final UserRepository userRepository;

    @Autowired
    public RetirementController(RetirementService retirementService, UserRepository userRepository) {
        this.retirementService = retirementService;
        this.userRepository = userRepository;
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateRetirement(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            int currentAge = Integer.parseInt(payload.get("currentAge").toString());
            int retirementAge = Integer.parseInt(payload.get("retirementAge").toString());
            double monthlyContribution = Double.parseDouble(payload.get("monthlyContribution").toString());
            double currentSavings = Double.parseDouble(payload.get("currentSavings").toString());
            double annualReturn = Double.parseDouble(payload.get("annualReturn").toString());

            double result = retirementService.calculateRetirementSavings(currentAge, retirementAge, monthlyContribution, currentSavings, annualReturn);

            // Save the retirement plan for the authenticated user
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
            RetirementPlan retirementPlan = new RetirementPlan();
            retirementPlan.setCurrentAge(currentAge);
            retirementPlan.setRetirementAge(retirementAge);
            retirementPlan.setMonthlyContribution(monthlyContribution);
            retirementPlan.setCurrentSavings(currentSavings);
            retirementPlan.setAnnualReturn(annualReturn);
            retirementPlan.setResult(result);
            retirementPlan.setUser(user);
            retirementService.saveRetirementPlan(retirementPlan, user.getId());

            return ResponseEntity.ok(Map.of("retirementResult", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred during the calculation.");
        }
    }
}