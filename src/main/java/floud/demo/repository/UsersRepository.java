package floud.demo.repository;

import floud.demo.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByNickname(String nickname);

    Optional<Users> findByNickname(String nickname);
}
