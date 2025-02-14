package funix.epfw.service;

import funix.epfw.model.Product;
import funix.epfw.model.User;
import funix.epfw.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //Add product service
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    //Get all products by user
    public List<Product> getAllProductsByUser(User currentUser) {
        return productRepository.findProductByCreatedBy(currentUser);
    }

    //Get all products
    public List<Product> findAll() {
         return productRepository.findAll();
    }
}
