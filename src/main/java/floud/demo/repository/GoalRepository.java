package floud.demo.repository;

import floud.demo.domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    @Query(value = "SELECT * FROM goal g WHERE g.users_id = :users_id", nativeQuery = true)
    List<Goal> findAllByUserId(Long users_id);
}
