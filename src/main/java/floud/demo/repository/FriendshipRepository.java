package floud.demo.repository;

import floud.demo.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query(value = "SELECT * FROM friendship f " +
            "WHERE f.friendship_status = 'ACCEPT' " +
            "AND (f.to_user = :users_id OR f.from_user = :users_id)", nativeQuery = true)
    List<Friendship> findAllByUsersId(Long users_id);

    @Query(value = "SELECT * FROM Friendship f " +
            "WHERE (f.to_user = :toUserId AND f.from_user = :fromUserId) " +
            "OR (f.to_user = :fromUserId AND f.from_user = :toUserId)",  nativeQuery = true)
    Optional<Friendship> findByToUserAndFromUser(Long toUserId, Long fromUserId);

    @Query(value = "SELECT * FROM friendship f " +
            "WHERE f.friendship_status = 'WAITING' AND  f.from_user = :users_id", nativeQuery = true)
    List<Friendship> findAllByWaitingToUser(Long users_id);


}
