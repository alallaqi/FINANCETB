package FTbackend.finance.data.repository;

import FTbackend.finance.data.domain.Mortgage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MortgageRepository extends JpaRepository<Mortgage, Long> {
    List<Mortgage> findByUserId(Long userId);
}
