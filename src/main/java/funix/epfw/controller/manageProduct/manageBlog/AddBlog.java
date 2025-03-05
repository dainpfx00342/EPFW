package funix.epfw.controller.manageProduct.manageBlog;

import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.productService.BlogService;
import funix.epfw.service.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AddBlog {

    private final BlogService blogService;
    private final ProductService productService;

    @Autowired
    public AddBlog(BlogService blogService, ProductService productService) {
        this.blogService = blogService;
        this.productService = productService;
    }

    @GetMapping("/addBlog/{id}")
    public String showAddBlogForm(Model model, @PathVariable Long id) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("blog", new Blog());
        return "manage_product/manage_blog/addBlog";
    }


    @PostMapping("/addBlog/{id}")
    public String addBlog(@ModelAttribute("blog") Blog blog, @PathVariable Long id) {
        Product product = productService.findById(id);
        blogService.saveBlog(product,blog);
        return "redirect:/manageProduct";
    }
}
