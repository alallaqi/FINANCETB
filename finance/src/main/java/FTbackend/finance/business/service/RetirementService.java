package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.RetirementPlan;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.RetirementPlanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import FTbackend.finance.data.repository.UserRepository;

import java.util.List;

@Service
public class RetirementService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RetirementPlanRepository retirementPlanRepository;

    public List<RetirementPlan> findUserRetirementPlans(Long userId) {
        return retirementPlanRepository.findByUserId(userId);
    }

    @Transactional
    public RetirementPlan saveRetirementPlan(RetirementPlan retirementPlan, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        retirementPlan.setUser(user);
        return retirementPlanRepository.save(retirementPlan);
    }
    public double calculateRetirementSavings(double currentAge, double retirementAge, double monthlyContribution, double currentSavings, double annualReturn) {
        int months = (int) ((retirementAge - currentAge) * 12);
        double monthlyReturnRate = annualReturn / 12 / 100;
        double futureValue = currentSavings * Math.pow(1 + monthlyReturnRate, months);
        for (int i = 0; i < months; i++) {
            futureValue += monthlyContribution * Math.pow(1 + monthlyReturnRate, i);
        }
        return futureValue;
    }
}
