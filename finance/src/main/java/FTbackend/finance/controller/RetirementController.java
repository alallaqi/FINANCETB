package FTbackend.finance.controller;

import FTbackend.finance.business.service.RetirementService;
import FTbackend.finance.data.domain.Calculation;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.CalculationRepository;
import FTbackend.finance.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/retirement")
public class RetirementController {

    @Autowired
    private RetirementService retirementService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalculationRepository calculationRepository;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateRetirement(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            int currentAge = Integer.parseInt(payload.get("currentAge").toString());
            int retirementAge = Integer.parseInt(payload.get("retirementAge").toString());
            double monthlyContribution = Double.parseDouble(payload.get("monthlyContribution").toString());
            double currentSavings = Double.parseDouble(payload.get("currentSavings").toString());
            double annualReturn = Double.parseDouble(payload.get("annualReturn").toString());

            double result = retirementService.calculateRetirementSavings(currentAge, retirementAge, monthlyContribution, currentSavings, annualReturn);

            // Save the calculation for the authenticated user
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Calculation calculation = new Calculation();
                calculation.setType("Retirement");
                calculation.setResult(result);
                calculation.setTimestamp(LocalDateTime.now());
                calculation.setUser(user);
                calculationRepository.save(calculation);
            }

            return ResponseEntity.ok(Map.of("retirementResult", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred during the calculation.");
        }
    }
}
