package floud.demo.repository;

import floud.demo.domain.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    //나랑 친구인 유저 목록
    @Query(value = "SELECT * FROM friendship f " +
            "WHERE f.friendship_status = 'ACCEPT' " +
            "AND (f.to_user = :users_id OR f.from_user = :users_id)", nativeQuery = true)
    List<Friendship> findAllByUsersId(Long users_id);

    //현재 친구 관계가 있는 지 확인
    @Query(value = "SELECT * FROM friendship f " +
            "WHERE (f.to_user = :toUserId AND f.from_user = :fromUserId) " +
            "OR (f.to_user = :fromUserId AND f.from_user = :toUserId)",  nativeQuery = true)
    Optional<Friendship> checkExistingFriendship(Long toUserId, Long fromUserId);

    //대기 상태인 친구들
    @Query(value = "SELECT * FROM friendship f " +
            "WHERE f.friendship_status = 'WAITING' AND  f.to_user = :users_id", nativeQuery = true)
    List<Friendship> findAllByWaitingToUser(Long users_id);

    Optional<Friendship> findById(Long id);

    //친구 거절/삭제한 친구 관계들
    @Query(value = "SELECT * FROM friendship f WHERE f.friendship_status = 'REJECT'", nativeQuery = true)
    List<Friendship> findAllRejectStatus();

}
