package FTbackend.finance.data.repository;

import java.util.List;
import FTbackend.finance.data.domain.Investment;
import FTbackend.finance.data.domain.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Investment, Long> {
    List<Loan> findByUserId(Long userId);
}






