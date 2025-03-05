package funix.epfw.service.productService;

import funix.epfw.model.farm.tour.Tour;
import funix.epfw.repository.productRepo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourService {
    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    // Lây ra tất cả các tour của một nông trại
    public List<Tour> getToursByFarm(Long farmId) {
        return tourRepository.findByFarmId(farmId);
    }
}
