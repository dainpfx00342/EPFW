package funix.epfw.controller.manageProduct.manageBlog;

import funix.epfw.model.User;
import funix.epfw.model.product.Blog;
import funix.epfw.service.BlogService;
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

    @Autowired
    public ManageBlog(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/manageBlog/{id}")
    public String manageBlog(@PathVariable Long id,HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");
        List<Blog> blogs = blogService.getBlogsByProduct(id);
        model.addAttribute("blogs",blogs);
        model.addAttribute("productId",id);
        return "/manage_product/manage_blog/manageBlog";
    }
}
