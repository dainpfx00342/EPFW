package funix.epfw.repository;

import funix.epfw.model.Product;
import funix.epfw.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product>findProducsByCreatedBy(User createdBy);
}
