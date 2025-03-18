package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.service.farm.product.blog.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailBlog {

    private final BlogService blogService;

    @Autowired
    public DetailBlog(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blogDetail/{blogId}")
    public String blogDetail(@PathVariable Long blogId, Model model) {
        Blog blog = blogService.findById(blogId);
        Long productId = blog.getProduct().getId();
        model.addAttribute("blog", blog);
        model.addAttribute("productId", productId);

        return ViewPaths.DETAIL_BLOG;
    }

}
