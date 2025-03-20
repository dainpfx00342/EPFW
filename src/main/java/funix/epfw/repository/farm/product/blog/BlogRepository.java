package funix.epfw.repository.farm.product.blog;

import funix.epfw.model.farm.product.Blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    // Lây ra tất cả các blog của một sản phẩm
    @Query("SELECT b FROM Blog b JOIN b.products p WHERE p.id IN :productIds")
    List<Blog> findByProductIds(@Param("productIds") List<Long> productIds);

    //Lấy tất cả blog trong 1 sản phẩm
    List<Blog> findByProducts_Id(long productsId);

    List<Blog> findByTours_id(Long toursId);
}
