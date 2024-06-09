package FTbackend.finance.data.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "calculations")
public class Calculation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private double result;
    private LocalDateTime timestamp;

    // Specific fields for different types of calculations
    private Double principal; // for mortgage and loan calculations
    private Double interestRate; // for mortgage, loan, and investment calculations
    private Integer term; // for mortgage and loan calculations
    private Double amount; // for investment and emergency fund calculations
    private Double rate; // for investment calculations
    private Integer years; // for investment calculations
    private Double currentSavings; // for retirement calculations
    private Double monthlySavings; // for retirement calculations
    private Integer yearsToRetire; // for retirement calculations
    private Double monthlyExpenses; // for emergency fund calculations
    private Integer targetMonths; // for emergency fund calculations

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getYears() {
        return years;
    }

    public void setYears(Integer years) {
        this.years = years;
    }

    public Double getCurrentSavings() {
        return currentSavings;
    }

    public void setCurrentSavings(Double currentSavings) {
        this.currentSavings = currentSavings;
    }

    public Double getMonthlySavings() {
        return monthlySavings;
    }

    public void setMonthlySavings(Double monthlySavings) {
        this.monthlySavings = monthlySavings;
    }

    public Integer getYearsToRetire() {
        return yearsToRetire;
    }

    public void setYearsToRetire(Integer yearsToRetire) {
        this.yearsToRetire = yearsToRetire;
    }

    public Double getMonthlyExpenses() {
        return monthlyExpenses;
    }

    public void setMonthlyExpenses(Double monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }

    public Integer getTargetMonths() {
        return targetMonths;
    }

    public void setTargetMonths(Integer targetMonths) {
        this.targetMonths = targetMonths;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
