package floud.demo.repository;

import floud.demo.domain.Memoir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MemoirRepository  extends JpaRepository<Memoir, Long> {

    @Query(value = "SELECT * FROM memoir m WHERE m.users_id =:users_id AND created_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<Memoir> findAllByWeek(Long users_id, LocalDateTime startDate, LocalDateTime endDate);
}
