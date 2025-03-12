package funix.epfw.controller.farm.product.manageBlog;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.product.ProductService;
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

    @GetMapping("/addBlog/{productId}")
    public String showAddBlogForm(Model model, @PathVariable Long productId) {
        Product currentProduct = productService.findById(productId);
        model.addAttribute("product", currentProduct);
        model.addAttribute("blog", new Blog());
        return ViewPaths.ADD_BLOG;
    }


    @PostMapping("/addBlog/{productId}")
    public String addBlog(@ModelAttribute("blog") Blog blog, @PathVariable Long productId ,
                          Model model)  {
        //kiem tra neu product khong ton tai
        Product product = productService.findById(productId);
        if(product==null){
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy san pham.");
            return ViewPaths.ADD_BLOG;
        }

        blogService.saveBlog(product, blog);
        return "redirect:/manageProduct";
    }
}
