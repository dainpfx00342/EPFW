package funix.epfw.repository.farm.product;

import funix.epfw.model.farm.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //list all product in each farm
     List<Product> findByFarmIdIn(List<Long> farmIds);
}
