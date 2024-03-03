package floud.demo.repository;

import floud.demo.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, Long> {
    boolean existsByNickname(String nickname);
}
