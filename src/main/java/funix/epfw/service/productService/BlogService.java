package funix.epfw.service.productService;

import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.repository.productRepo.BlogRepository;
import funix.epfw.repository.productRepo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository, ProductRepository productRepository) {
        this.blogRepository = blogRepository;

    }

    // Lưu một blog vào một sản phẩm
    public void saveBlog(Product product, Blog blog) {

         blog.setProduct(product);
         blogRepository.save(blog);
    }

    // Lấy ra tất cả các blog của một sản phẩm
    public List<Blog> getBlogsByProduct(Long productId) {
        return blogRepository.findByProductId(productId);
    }

    // Lấy ra một blog theo id
    public Blog findById(Long id) {
        Optional<Blog> blog = blogRepository.findById(id);
        return blog.orElse(null);
    }


    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    //get all blogs
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }
}
