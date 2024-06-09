package FTbackend.finance.controller;

import FTbackend.finance.business.service.LoanService;
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
@RequestMapping("/api/loan")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalculationRepository calculationRepository;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateLoan(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            double principal = Double.parseDouble(payload.get("amount").toString());
            double interestRate = Double.parseDouble(payload.get("interest").toString());
            int term = Integer.parseInt(payload.get("term").toString());

            double result = loanService.calculateMonthlyPayment(principal, interestRate, term);

            // Save the calculation for the authenticated user
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Calculation calculation = new Calculation();
                calculation.setType("Loan");
                calculation.setResult(result);
                calculation.setTimestamp(LocalDateTime.now());
                calculation.setUser(user);
                calculationRepository.save(calculation);
            }

            return ResponseEntity.ok(Map.of("loanResult", result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred during the calculation.");
        }
    }
}
