package floud.demo.repository;

import floud.demo.domain.Memoir;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoirRepository  extends JpaRepository<Long, Memoir> {

}
