package funix.epfw.controller.farm.product;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.productService.BlogService;
import funix.epfw.service.productService.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductDetail {
    private final ProductService productService;
    private final BlogService blogService;

    @Autowired
    public ProductDetail(ProductService productService, BlogService blogService) {
        this.productService = productService;
        this.blogService = blogService;
    }

    @GetMapping("/productDetail/{id}")
    public String showProductDetail(Model model, @PathVariable Long id) {
        Product product = productService.findById(id);
        List<Blog> blogs = blogService.getBlogsByProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("blogs", blogs);
        return ViewPaths.DETAIL_PRODUCT;
    }
}
