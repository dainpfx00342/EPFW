package funix.epfw.controller.home;

import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.productService.BlogService;
import funix.epfw.service.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final BlogService BlogService;

    @Autowired
    public HomeController(ProductService productService, BlogService BlogService) {
        this.productService = productService;
        this.BlogService = BlogService;
    }

    @GetMapping({"/","/home"})
    public String gohome(Model model) {
        List<Product> products = productService.findAll();
        List<Blog> blogs = BlogService.getAllBlogs();
        model.addAttribute("blogs", blogs);
        model.addAttribute("products", products);
        return "home/home";
    }

}
