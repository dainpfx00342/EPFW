package funix.epfw.repository.productRepo;

import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    // Lây ra tất cả các blog của một sản phẩm
    List<Blog> findByProductId(Long productId);

    Object findByProduct(Product value);
}
