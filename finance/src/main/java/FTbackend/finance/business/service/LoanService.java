package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.Loan;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.LoanRepository;
import FTbackend.finance.data.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    @Autowired
    public LoanService(UserRepository userRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    public List<Loan> getUserLoans(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    @Transactional
    public Loan saveLoan(Loan loan, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        loan.setUser(user);
        return loanRepository.save(loan);
    }

    public double calculateMonthlyPayment(double principal, double annualRate, int years) {
        double monthlyRate = annualRate / 100 / 12;
        int totalPayments = years * 12;
        if (annualRate == 0) {
            return principal / totalPayments;
        }

        return (principal * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -totalPayments));
    }
}