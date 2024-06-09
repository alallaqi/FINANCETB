package FTbackend.finance.business.service;

import FTbackend.finance.data.domain.EmergencyFund;
import FTbackend.finance.data.domain.User;
import FTbackend.finance.data.repository.EmergencyFundRepository;
import FTbackend.finance.data.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmergencyFundService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmergencyFundRepository emergencyFundRepository;

    public List<EmergencyFund> getUserEmergencyFunds(Long userId) {
        return emergencyFundRepository.findByUserId(userId);
    }
    @Transactional
    public EmergencyFund saveEmergencyFund(EmergencyFund emergencyFund, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        emergencyFund.setUser(user);
        return emergencyFundRepository.save(emergencyFund);
    }

    public EmergencyFund calculateEmergencyFundGoal(EmergencyFund emergencyFund) {
        double totalMonthlyExpenses = emergencyFund.getHousing() + emergencyFund.getUtilities() +
                emergencyFund.getGroceries() + emergencyFund.getTransportation() +
                emergencyFund.getDebtPayments() + emergencyFund.getOtherEssentials();
        emergencyFund.setTotalMonthlyExpenses(totalMonthlyExpenses);
        double emergencyFundGoal = totalMonthlyExpenses * emergencyFund.getCoverageMonths();
        emergencyFund.setEmergencyFundGoal(emergencyFundGoal);
        return emergencyFundRepository.save(emergencyFund);
    }
}
