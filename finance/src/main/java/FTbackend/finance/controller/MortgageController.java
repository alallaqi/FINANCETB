package FTbackend.finance.controller;

import FTbackend.finance.business.service.MortgageService;
import FTbackend.finance.data.domain.Calculation;
import FTbackend.finance.data.domain.Mortgage;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.CalculationRepository;
import FTbackend.finance.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/mortgage")
public class MortgageController {

    private static final Logger log = LoggerFactory.getLogger(MortgageController.class);

    @Autowired
    private MortgageService mortgageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalculationRepository calculationRepository;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateMortgage(@RequestBody Map<String, Object> payload, Authentication authentication) {
        log.info("Received mortgage calculation request with payload={}", payload);

        double principal = Double.parseDouble(payload.get("principal").toString());
        double annualInterestRate = Double.parseDouble(payload.get("interestRate").toString());
        int termInYears = Integer.parseInt(payload.get("term").toString());

        double result = mortgageService.calculateMonthlyPayment(principal, annualInterestRate, termInYears);

        // Save the calculation for the authenticated user
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            Calculation calculation = new Calculation();
            calculation.setType("Mortgage");
            calculation.setResult(result);
            calculation.setUser(user);
            calculationRepository.save(calculation);
        }
        return ResponseEntity.ok(Map.of("mortgageResult", result));
    }
}
