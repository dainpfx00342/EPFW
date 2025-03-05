package funix.epfw.controller.manageProduct;

import funix.epfw.constants.ProductCategory;
import funix.epfw.constants.Unit;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FramerAuth;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.productService.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@SessionAttributes("loggedInUser")
public class AddProduct {

    private final ProductService productService;

    @Autowired
    public AddProduct(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model, HttpSession session) {
        AuthChecker authoChecker = new FramerAuth();
        String accessCheck = authoChecker.checkAuth(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        List<Unit> units = Arrays.asList(Unit.values());// Lấy danh sách đơn vị
        List<ProductCategory> categories = Arrays.asList(ProductCategory.values());// Lấy danh sách loại sản phẩm
        model.addAttribute("categories", categories);
        model.addAttribute("units", units);
        model.addAttribute("product", new Product());
        return "/manage_product/addProduct";
    }


    @PostMapping("/addProduct")
    public String addProduct( @Validated @ModelAttribute("product") Product product,
                              BindingResult result,
                              @RequestParam("imageFile") MultipartFile file,
                              Model model, HttpSession session) {
        try{
            String imageUrl = productService.saveImage(file);
            if(imageUrl != null){
                product.setImageUrl(imageUrl);
            }
        }catch (IOException e){
            model.addAttribute("registrationError", "Lỗi lưu ảnh");
        }
        // Kiểm tra xác thực người dùng
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        // Kiểm tra nếu form có lỗi validation
        if (result.hasErrors()) {
            model.addAttribute("registrationError", "Vui lòng nhập đầy đủ và chính xác thông tin.");
            return "/manage_product/addProduct";
        }
        // Gán người tạo sản phẩm
        product.setCreatedBy(user);

        // Lưu vào DB
        productService.saveProduct(product);

        return "redirect:/manageProduct";

    }
}
