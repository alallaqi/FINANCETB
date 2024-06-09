package FTbackend.finance.controller;

import FTbackend.finance.business.service.EmergencyFundService;
import FTbackend.finance.data.domain.Calculation;
import FTbackend.finance.data.domain.EmergencyFund;
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
@RequestMapping("/api/emergencyfund")
public class EmergencyFundController {

    @Autowired
    private EmergencyFundService emergencyFundService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalculationRepository calculationRepository;

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateEmergencyFund(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            double housing = Double.parseDouble(payload.get("housing").toString());
            double utilities = Double.parseDouble(payload.get("utilities").toString());
            double groceries = Double.parseDouble(payload.get("groceries").toString());
            double transportation = Double.parseDouble(payload.get("transportation").toString());
            double debtPayments = Double.parseDouble(payload.get("debtPayments").toString());
            double otherEssentials = Double.parseDouble(payload.get("otherEssentials").toString());
            int coverageMonths = Integer.parseInt(payload.get("coverageMonths").toString());

            EmergencyFund emergencyFund = new EmergencyFund();
            emergencyFund.setHousing(housing);
            emergencyFund.setUtilities(utilities);
            emergencyFund.setGroceries(groceries);
            emergencyFund.setTransportation(transportation);
            emergencyFund.setDebtPayments(debtPayments);
            emergencyFund.setOtherEssentials(otherEssentials);
            emergencyFund.setCoverageMonths(coverageMonths);

            // Set the user on the emergency fund
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
            emergencyFund.setUser(user);

            EmergencyFund result = emergencyFundService.calculateEmergencyFundGoal(emergencyFund);

            // Save the calculation for the authenticated user
            if (user != null) {
                Calculation calculation = new Calculation();
                calculation.setType("EmergencyFund");
                calculation.setResult(result.getEmergencyFundGoal());
                calculation.setTimestamp(LocalDateTime.now());
                calculation.setUser(user);
                calculationRepository.save(calculation);
            }

            return ResponseEntity.ok(Map.of("emergencyFundGoal", result.getEmergencyFundGoal()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred during the calculation.");
        }
    }
}
