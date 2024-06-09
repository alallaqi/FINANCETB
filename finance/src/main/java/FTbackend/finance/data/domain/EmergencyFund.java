package FTbackend.finance.data.domain;

import jakarta.persistence.*;

@Entity
public class EmergencyFund {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double housing;
    private double utilities;
    private double groceries;
    private double transportation;
    private double debtPayments;
    private double otherEssentials;
    private double totalMonthlyExpenses;
    private int coverageMonths;
    private double emergencyFundGoal;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public EmergencyFund() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getHousing() {
        return housing;
    }

    public void setHousing(double housing) {
        this.housing = housing;
    }

    public double getUtilities() {
        return utilities;
    }

    public void setUtilities(double utilities) {
        this.utilities = utilities;
    }

    public double getGroceries() {
        return groceries;
    }

    public void setGroceries(double groceries) {
        this.groceries = groceries;
    }

    public double getTransportation() {
        return transportation;
    }

    public void setTransportation(double transportation) {
        this.transportation = transportation;
    }

    public double getDebtPayments() {
        return debtPayments;
    }

    public void setDebtPayments(double debtPayments) {
        this.debtPayments = debtPayments;
    }

    public double getOtherEssentials() {
        return otherEssentials;
    }

    public void setOtherEssentials(double otherEssentials) {
        this.otherEssentials = otherEssentials;
    }

    public double getTotalMonthlyExpenses() {
        return totalMonthlyExpenses;
    }

    public void setTotalMonthlyExpenses(double totalMonthlyExpenses) {
        this.totalMonthlyExpenses = totalMonthlyExpenses;
    }

    public int getCoverageMonths() {
        return coverageMonths;
    }

    public void setCoverageMonths(int coverageMonths) {
        this.coverageMonths = coverageMonths;
    }

    public double getEmergencyFundGoal() {
        return emergencyFundGoal;
    }

    public void setEmergencyFundGoal(double emergencyFundGoal) {
        this.emergencyFundGoal = emergencyFundGoal;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
