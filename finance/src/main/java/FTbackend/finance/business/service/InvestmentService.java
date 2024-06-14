package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.Investment;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.InvestmentRepository;
import FTbackend.finance.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class InvestmentService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentService.class);

    private final UserRepository userRepository;
    private final InvestmentRepository investmentRepository;

    @Autowired
    public InvestmentService(UserRepository userRepository, InvestmentRepository investmentRepository) {
        this.userRepository = userRepository;
        this.investmentRepository = investmentRepository;
    }

    /**
     * Save the investment calculation associated with a specific user.
     *
     * @param investment The Investment instance to be saved.
     * @param userId     The ID of the user.
     * @return The saved Investment instance.
     */
    @Transactional
    public Investment saveInvestmentCalculation(Investment investment, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        investment.setUser(user);
        return investmentRepository.save(investment);
    }

    /**
     * Calculate the investment value over a period of years.
     *
     * @param amount The principal amount invested.
     * @param rate   The annual interest rate (in percentage).
     * @param years  The number of years the money is invested for.
     * @return The calculated investment value.
     */
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