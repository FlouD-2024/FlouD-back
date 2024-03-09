package floud.demo.repository;

import floud.demo.domain.Community;
import floud.demo.domain.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    @Query(value = "SELECT * FROM Community c WHERE c.post_type = :postType ORDER BY c.created_at DESC limit 30", nativeQuery = true)
    List<Community> findTop30ByPostType(String postType);

    @Query(value = "SELECT * FROM Community c WHERE c.users_id = :users_id", nativeQuery = true)
    List<Community> findAllByUser(Long users_id);
}
