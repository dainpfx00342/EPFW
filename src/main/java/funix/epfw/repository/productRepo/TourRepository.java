package funix.epfw.repository.productRepo;

import funix.epfw.model.farm.tour.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    // Lây ra tất cả các tour của một sản phẩm
    List<Tour> findByFarmIdIn( List<Long> farmIds);
    List<Tour> findByFarmId(Long farmId);

}
