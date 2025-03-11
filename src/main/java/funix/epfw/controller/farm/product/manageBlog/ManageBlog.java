package funix.epfw.controller.farm.product.manageBlog;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.productService.BlogService;
import funix.epfw.service.productService.ProductService;
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

//    @GetMapping("/manageBlog")
//    public String manageBlog(HttpSession session, Model model) {
//        User currentUser = (User) session.getAttribute("loggedInUser");
//
//
//        List<Blog> blogs = blogService.getAllBlogs();
//
//        model.addAttribute("blogs", blogs);
//        return ViewPaths.MANAGE_BLOG;
//    }

    @GetMapping("/manageBlog/{id}")
    public String manageBlog(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        List<Blog> blogs = blogService.getBlogsByProduct(id);
        model.addAttribute("blogs",blogs);
        model.addAttribute("product",product);
        model.addAttribute("productId",id);
        return ViewPaths.MANAGE_BLOG;
    }
}
