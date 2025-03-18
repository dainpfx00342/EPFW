package funix.epfw.service.farm.tour;

import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.repository.farm.tour.TourRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TourService {
    private final TourRepository tourRepository;

    @Autowired
    public TourService(TourRepository tourRepository) {

        this.tourRepository = tourRepository;
    }

    public void saveTour(Tour tour) {

        tourRepository.save(tour);
    }

    public List<Tour> findByFarms(List<Farm> farms) {
        List<Long> farmIds = farms.stream().map(Farm::getId).collect(Collectors.toList());
        return tourRepository.findByFarmIdIn(farmIds);
    }

    public List<Tour> findAll() {

        return tourRepository.findAll();
    }

    public Tour findById(Long tourId) {

        return tourRepository.findById(tourId).orElse(null);
    }
}
