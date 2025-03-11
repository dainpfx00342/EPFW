package funix.epfw.service.productService;

import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.repository.productRepo.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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

    //Get all products
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    //get product by fram
    @Transactional
    public List<Product> findByFarms(List<Farm> farms) {
        List<Long> farmIds = farms.stream().map(Farm::getId).collect(Collectors.toList());
        return productRepository.findByFarmIdIn(farmIds);
    }
    //Delete product by id
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    //Find product by id
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveOrUpdateProduct(Product product) {
        productRepository.save(product); // This will use saveOrUpdate internally
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
