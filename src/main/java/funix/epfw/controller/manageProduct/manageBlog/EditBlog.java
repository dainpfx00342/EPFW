package funix.epfw.controller.manageProduct.manageBlog;

import funix.epfw.model.product.Blog;
import funix.epfw.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Base64;

@Controller
public class EditBlog {
    private final BlogService blogService;

    @Autowired
    public EditBlog(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/editBlog/{id}")
    public String editBlog(@PathVariable Long id, Model model) {
        Blog blog = blogService.findById(id);
        model.addAttribute("blog", blog);

        return "manage_product/manage_blog/editBlog";
    }

    @PostMapping("/editBlog/{id}")
    public String editBlog(@PathVariable Long id, Blog blog) {
        Blog blogToUpdate = blogService.findById(id);
        blogToUpdate.setTitle(blog.getTitle());
        blogToUpdate.setContent(blog.getContent());

        blogService.saveBlog(blogToUpdate.getProduct(), blogToUpdate);

        return "redirect:/manageBlog/" + blogToUpdate.getProduct().getId();
    }
}
