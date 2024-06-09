package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.Mortgage;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.MortgageRepository;
import FTbackend.finance.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.transaction.Transactional;

@Service
public class MortgageService {

    private static final Logger log = LoggerFactory.getLogger(MortgageService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MortgageRepository mortgageRepository;

    @Transactional
    public Mortgage saveMortgage(Mortgage mortgage, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        mortgage.setUser(user);
        return mortgageRepository.save(mortgage);
    }

    public double calculateMonthlyPayment(double principal, double interestRate, int termYears) {
        log.info("Calculating monthly payment with principal: {}, interest rate: {}, term years: {}", principal, interestRate, termYears);

        if (termYears == 0) {
            throw new IllegalArgumentException("Term years must be greater than zero.");
        }

        if (interestRate == 0) {
            double payment = principal / (termYears * 12);
            log.info("Interest rate is 0, monthly payment is: {}", payment);
            return payment;
        }

        double monthlyRate = interestRate / 100 / 12;
        int totalPayments = termYears * 12;

        double monthlyPayment = (principal * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -totalPayments));
        log.info("Calculated monthly payment: {}", monthlyPayment);

        return monthlyPayment;
    }


}
