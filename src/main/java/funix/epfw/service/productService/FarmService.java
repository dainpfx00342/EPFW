package funix.epfw.service.productService;

import funix.epfw.model.farm.Farm;
import funix.epfw.repository.productRepo.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
}
