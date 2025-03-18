package funix.epfw.service.farm;

import funix.epfw.model.farm.Farm;
import funix.epfw.repository.farm.FarmRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Transactional
public class FarmService {
    private final FarmRepository farmRepository;


    @Autowired
    public FarmService(FarmRepository farmRepository) {

        this.farmRepository = farmRepository;
    }

    //tim tat ca cac farm theo user id
    public List<Farm> findByUserId(Long userId) {

        return farmRepository.findByUserId(userId);
    }

    public void saveFarm(Farm farm) {

        farmRepository.save(farm);
    }

    public Farm findById(Long id){

        return farmRepository.findById(id).orElse(null);
    }

    public List<Farm> findAll() {

        return farmRepository.findAll();
    }

    public void deleteFarmById(Long farmId) {

        farmRepository.deleteById(farmId);
    }
}
