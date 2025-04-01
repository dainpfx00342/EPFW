package funix.epfw.controller.farm.product;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.farm.product.blog.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class DetailProduct {

    private final ProductService productService;
    private final BlogService blogService;

    @Autowired
    public DetailProduct(ProductService productService, BlogService blogService) {
        this.productService = productService;
        this.blogService = blogService;
    }

    @GetMapping("/detailProduct/{productId}")
    public String showProductDetail(Model model, @PathVariable Long productId) {
        // Lấy thông tin sản phẩm
        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy sản phẩm.");
            return ViewPaths.MANAGE_PRODUCT; // Chuyển hướng trang lỗi
        }

        // Lấy danh sách blog của sản phẩm
        List<Blog> blogs = blogService.getBlogsByProductId(productId);

        model.addAttribute("product", product);
        model.addAttribute("blogs", blogs);

        return ViewPaths.DETAIL_PRODUCT;
    }
}