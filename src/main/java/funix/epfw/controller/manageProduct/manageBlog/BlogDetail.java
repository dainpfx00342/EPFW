package funix.epfw.controller.manageProduct.manageBlog;

import funix.epfw.model.farm.product.Blog;
import funix.epfw.service.productService.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BlogDetail {

    private final BlogService blogService;

    @Autowired
    public BlogDetail(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blogDetail/{id}")
    public String blogDetail(@PathVariable Long id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);

        return "manage_product/manage_blog/blogDetail";
    }
}
