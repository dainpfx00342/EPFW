package funix.epfw.service;

import funix.epfw.model.Product;
import funix.epfw.model.User;
import funix.epfw.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //save product to database
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    //Get all products by user
    public List<Product> getAllProductsByUser(User currentUser) {
        return productRepository.findProducsByCreatedBy(currentUser);
    }

    //Get all products
    public List<Product> findAll() {
         return productRepository.findAll();
    }
    //Delete product by id
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    //Find product by id
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    //save image to database
    public String saveImage(MultipartFile file) throws IOException {
        if(file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get("src/main/resources/static/images/" + fileName);
            Files.write(filePath,file.getBytes());
            return "images/" + fileName; //tra ve duong dan file de luu vao database
        }
        return null;

    }

}
