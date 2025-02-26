package funix.epfw.repository;

import funix.epfw.model.product.Product;
import funix.epfw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.awt.color.ProfileDataException;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm sản phẩm theo người tạo
    List<Product>findProducsByCreatedBy(User createdBy);

}
