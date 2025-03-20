package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.product.ProductService;
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


    @GetMapping("/manageBlog/{productId}")
    public String manageBlog(@PathVariable Long productId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }

        Product product = productService.findById(productId);
        List<Blog> blogs = blogService.getBlogsByProductId(productId);
        model.addAttribute("blogs",blogs);
        model.addAttribute("product",product);
        model.addAttribute("productId",productId);
        return ViewPaths.MANAGE_BLOG;
    }
}
