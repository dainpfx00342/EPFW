package funix.epfw.service.productService;

import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.repository.productRepo.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourService {
    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    // Lây ra tất cả các tour của một nông trại
    public List<Tour> getToursByFarm(Farm farm) {
        return tourRepository.findByFarmId(farm.getId());
    }

    public void addTour(Tour tour) {
        tourRepository.save(tour);
    }

    public List<Tour> findByFarms(List<Farm> farms) {
        List<Long> farmIds = farms.stream().map(Farm::getId).collect(Collectors.toList());
        return tourRepository.findByFarmIdIn(farmIds);
    }

    public List<Tour> findAll() {
        return tourRepository.findAll();
    }
}
