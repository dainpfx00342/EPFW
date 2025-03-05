package funix.epfw.repository.productRepo;

import funix.epfw.model.farm.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarmRepository extends JpaRepository<Farm, Long> {
    //tim tat ca cac farm theo user id
    List<Farm> findByUserId(Long userId);
}
