package funix.epfw.repository.farm.product.blog;

import funix.epfw.model.farm.product.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {


    //Lấy tất cả blog trong 1 sản phẩm
    List<Blog> findByProducts_Id(long productsId);

    List<Blog> findByTours_id(Long toursId);

    List<Blog> findByTitleContainingIgnoreCase(String keyword);
}
