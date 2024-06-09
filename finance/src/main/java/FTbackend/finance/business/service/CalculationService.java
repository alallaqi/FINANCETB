package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.Calculation;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.CalculationRepository;
import FTbackend.finance.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CalculationService {

    @Autowired
    private CalculationRepository calculationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Calculation saveCalculation(Long userId, String type, double result) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Delete existing calculation of the same type for the user
        calculationRepository.deleteByUserIdAndType(userId, type);

        // Save the new calculation
        Calculation calculation = new Calculation();
        calculation.setType(type);
        calculation.setResult(result);
        calculation.setTimestamp(LocalDateTime.now());
        calculation.setUser(user);

        return calculationRepository.save(calculation);
    }
}
