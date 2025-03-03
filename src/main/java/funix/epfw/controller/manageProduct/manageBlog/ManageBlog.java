package funix.epfw.controller.manageProduct.manageBlog;

import funix.epfw.model.User;
import funix.epfw.model.product.Blog;
import funix.epfw.model.product.Product;
import funix.epfw.service.BlogService;
import funix.epfw.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("loggedInUser")
public class ManageBlog {
    private final BlogService blogService;
    private final ProductService productService;

    @Autowired
    public ManageBlog(BlogService blogService, ProductService productService) {
        this.blogService = blogService;
        this.productService = productService;
    }

    @GetMapping("/manageBlog/{id}")
    public String manageBlog(@PathVariable Long id,HttpSession session, Model model) {
        Product product = productService.findById(id);
        List<Blog> blogs = blogService.getBlogsByProduct(id);
        model.addAttribute("blogs",blogs);
        model.addAttribute("product",product);
        model.addAttribute("productId",id);
        return "/manage_product/manage_blog/manageBlog";
    }
}
