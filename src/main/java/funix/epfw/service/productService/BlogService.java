package funix.epfw.service.productService;

import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.repository.productRepo.BlogRepository;
import funix.epfw.repository.productRepo.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final ProductRepository productRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository, ProductRepository productRepository) {
        this.blogRepository = blogRepository;
        this.productRepository = productRepository;
    }

    // Lưu một blog vào một sản phẩm
    @Transactional
    public void saveBlog(Product product, Blog blog) {
        Product managedProduct = productRepository.findById(product.getId()).orElseThrow(() ->
                new RuntimeException("Product không tồn tại!")
        );

        blog.setProduct(managedProduct);

        if (blog.getId() != null) {
            Blog existingBlog = blogRepository.findById(blog.getId()).orElseThrow(() ->
                    new RuntimeException("Blog không tồn tại!")
            );

            existingBlog.setTitle(blog.getTitle());
            existingBlog.setContent(blog.getContent());
            existingBlog.setProduct(managedProduct);

            blogRepository.save(existingBlog);
        } else {
            blogRepository.save(blog);
        }
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
