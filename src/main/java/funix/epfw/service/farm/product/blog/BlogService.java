package funix.epfw.service.farm.product.blog;

import funix.epfw.model.farm.product.Blog;
import funix.epfw.repository.farm.product.blog.BlogRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Transactional
public class BlogService {

    private final BlogRepository blogRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    /**
     * Lưu một blog vào database
     */
    public void saveBlog(Blog blog) {
        blogRepository.save(blog);
    }

    /**
     * Lấy ra tất cả các blog có liên kết với danh sách sản phẩm
     * @param productIds Danh sách ID của sản phẩm
     * @return Danh sách Blog liên kết với các sản phẩm
     */
    public List<Blog> getBlogsByProduct(List<Long> productIds) {
        return blogRepository.findByProductIds(productIds);
    }

    /**
     * Lấy Blog theo ID
     */
    public Blog findById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    /**
     * Xóa Blog theo ID
     */
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    /**
     * Lấy tất cả Blog từ database
     */
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public List<Blog> getBlogsByProductId(Long productId) {
        return blogRepository.findByProducts_Id(productId);
    }

    public List<Blog> getBlogsByTourId(Long tourId){
        return  blogRepository.findByTours_id(tourId);
    }
}
