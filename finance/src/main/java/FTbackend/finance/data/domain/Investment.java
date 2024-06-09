package FTbackend.finance.data.domain;

import jakarta.persistence.*;

@Entity
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double initialAmount;
    private double monthlyContribution;
    private double annualReturn;
    private int years;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getInitialAmount() { return initialAmount; }
    public void setInitialAmount(double initialAmount) { this.initialAmount = initialAmount; }
    public double getMonthlyContribution() { return monthlyContribution; }
    public void setMonthlyContribution(double monthlyContribution) { this.monthlyContribution = monthlyContribution; }
    public double getAnnualReturn() { return annualReturn; }
    public void setAnnualReturn(double annualReturn) { this.annualReturn = annualReturn; }
    public int getYears() { return years; }
    public void setYears(int years) { this.years = years; }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
