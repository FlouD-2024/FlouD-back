package floud.demo.repository;

import floud.demo.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query(value = "SELECT * FROM friendship f " +
            "WHERE f.friendship_status = 'ACCPET' " +
            "AND f.to_user = :users_id OR f.from_user = :users_id", nativeQuery = true)
    List<Friendship> findAllByUsersId(Long users_id);
}
