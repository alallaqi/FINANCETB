package FTbackend.finance.data.repository;

import FTbackend.finance.data.domain.RetirementPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetirementPlanRepository extends JpaRepository<RetirementPlan, Long> {
    List<RetirementPlan> findByUserId(Long userId);
}
