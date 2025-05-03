package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class AddBlogProduct {

    private final BlogService blogService;
    private final ProductService productService;

    @Autowired
    public AddBlogProduct(BlogService blogService, ProductService productService) {
        this.blogService = blogService;
        this.productService = productService;
    }

    @GetMapping("/addBlog/product/{productId}")
    public String showAddBlogForm(Model model, @PathVariable Long productId, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }
        Product currentProduct = productService.findById(productId);
        if(currentProduct==null){
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy sản phẩm.");
            return ViewPaths.ADD_BLOG;
        }

        model.addAttribute("product", currentProduct);
        model.addAttribute("blog", new Blog());
        model.addAttribute("productId", productId);
        return ViewPaths.ADD_BLOG;
    }


    @PostMapping("/addBlog/product/{productId}")
    public String addBlog(@ModelAttribute("blog") Blog blog, @PathVariable Long productId ,
                          Model model)  {
        //kiem tra neu product khong ton tai
        Product product = productService.findById(productId);
        if(product==null){
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy sản phẩm.");
            return ViewPaths.ADD_BLOG;
        }
        // Liên kết blog với product
        if (blog.getProducts() == null) {

            blog.setProducts(new ArrayList<>());
        }
        blog.getProducts().add(product);
        product.getBlogs().add(blog);

        blogService.saveBlog(blog);
        return "redirect:/manageBlog/product/"+productId;
    }
}
