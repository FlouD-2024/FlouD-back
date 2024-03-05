package floud.demo.repository;

import floud.demo.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByNickname(String nickname);

    Optional<Users> findByNickname(String nickname);

    @Query("SELECT m FROM Users m WHERE (:social_id IS NULL OR m.social_id = :social_id)")
    Optional<Users> findBySocial_id(String social_id);
}
