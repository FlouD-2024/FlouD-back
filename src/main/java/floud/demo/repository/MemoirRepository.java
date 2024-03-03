package floud.demo.repository;

import floud.demo.domain.Memoir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemoirRepository  extends JpaRepository<Memoir, Long> {

    @Query(value = "SELECT * FROM memoir m WHERE m.users_id =:users_id AND created_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Memoir> findAllByWeek(Long users_id, LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'TRUE' ELSE 'FALSE' " +
            "END FROM Memoir m WHERE m.users_id = :users_id " +
            "AND m.created_at = :now", nativeQuery = true)
    boolean existsByUserAndCreatedAtBetween(Long users_id, LocalDate now);

    @Query(value = "SELECT * FROM Memoir m WHERE m.users_id = :users_id " +
            "AND m.created_at = :dateTime", nativeQuery = true)
    Optional<Memoir> findByCreatedAt(Long users_id, LocalDate dateTime);


}
