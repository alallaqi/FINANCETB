package FTbackend.finance.controller;

import FTbackend.finance.business.service.InvestmentService;
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
@RequestMapping("/api/investment")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalculationRepository calculationRepository;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateInvestment(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            double amount = Double.parseDouble(payload.get("amount").toString());
            double rate = Double.parseDouble(payload.get("rate").toString());
            int years = Integer.parseInt(payload.get("years").toString());

            double result = investmentService.calculateInvestment(amount, rate, years);

            // Save the calculation for the authenticated user
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Calculation calculation = new Calculation();
                calculation.setType("Investment");
                calculation.setResult(result);
                calculation.setTimestamp(LocalDateTime.now());
                calculation.setUser(user);
                calculationRepository.save(calculation);
            }

            return ResponseEntity.ok(Map.of("investmentResult", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred during the calculation.");
        }
    }
}
