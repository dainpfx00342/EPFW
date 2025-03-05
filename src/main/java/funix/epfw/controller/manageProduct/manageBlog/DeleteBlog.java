package funix.epfw.controller.manageProduct.manageBlog;

import funix.epfw.service.productService.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeleteBlog {
    private final BlogService blogService;

    @Autowired
    public DeleteBlog(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable Long id, @RequestParam("productId") Long productId ){
        blogService.deleteBlog(id);
        return "redirect:/manageBlog/"+productId;
    }



}
