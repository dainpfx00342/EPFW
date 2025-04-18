package funix.epfw.controller.home;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final BlogService BlogService;
    private final FarmService farmService;


    @Autowired
    public HomeController(ProductService productService, BlogService BlogService, FarmService farmService) {
        this.productService = productService;
        this.BlogService = BlogService;
        this.farmService = farmService;
    }

    @GetMapping({"/","/home"})
    public String gohome(Model model) {
        List<Product> products = productService.findAll();
        List<Farm> farms = farmService.findAll();
        List<Blog> blogs = BlogService.getAllBlogs().stream().sorted(Comparator.comparing(Blog::getId).reversed()).toList();
        model.addAttribute("blogs", blogs);
        model.addAttribute("products", products);
        model.addAttribute("farms", farms);
        return ViewPaths.HOME;
    }

    @GetMapping("/searchBlog")
    public String searchBlog(Model model, @RequestParam("keyword") String keyword) {
        List<Blog> blogs = BlogService.getBlogsByKeyword(keyword);
        if (keyword != null && !keyword.isEmpty()) {
            blogs = BlogService.getBlogsByKeyword(keyword).stream()
                    .filter(blog -> blog.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }
        model.addAttribute("blogs", blogs);
        model.addAttribute("keyword", keyword);
        return ViewPaths.HOME;
    }
}
