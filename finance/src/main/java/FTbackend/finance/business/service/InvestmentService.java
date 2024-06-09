package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.Calculation;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.CalculationRepository;
import FTbackend.finance.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.transaction.Transactional;

@Service
public class InvestmentService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalculationRepository calculationRepository;

    @Transactional
    public Calculation saveInvestmentCalculation(Calculation calculation, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        calculation.setUser(user);
        calculation.setType("Investment");
        return calculationRepository.save(calculation);
    }

    public double calculateInvestment(double amount, double rate, int years) {
        log.info("Calculating investment with amount: {}, rate: {}, years: {}", amount, rate, years);

        if (years == 0) {
            throw new IllegalArgumentException("Years must be greater than zero.");
        }

        double result = amount * Math.pow((1 + rate / 100), years);
        log.info("Calculated investment result: {}", result);

        return result;
    }
}
