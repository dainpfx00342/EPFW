package funix.epfw.repository.productRepo;

import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm sản phẩm theo người tạo
    List<Product>findProducsByCreatedBy(User createdBy);

}
