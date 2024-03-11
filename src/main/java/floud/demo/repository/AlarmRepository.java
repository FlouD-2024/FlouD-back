package floud.demo.repository;

import floud.demo.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query(value = "SELECT * FROM Alarm a WHERE a.users_id = :users_id " +
            "ORDER BY a.created_at DESC limit 2", nativeQuery = true)
    List<Alarm> find2ByUser(Long users_id);

    @Query(value = "SELECT * FROM Alarm a WHERE a.users_id = :users_id " +
            "ORDER BY c.created_at DESC limit 30", nativeQuery = true)
    List<Alarm> find30ByUser(Long users_id);
}
