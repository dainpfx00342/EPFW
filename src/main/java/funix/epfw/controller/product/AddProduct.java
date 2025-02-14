package funix.epfw.controller.product;

import funix.epfw.constants.Role;
import funix.epfw.controller.auth.AuthChecker;
import funix.epfw.controller.auth.FramerAuth;
import funix.epfw.model.Product;
import funix.epfw.model.User;
import funix.epfw.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        model.addAttribute("product", new Product());
        return "/manage_product/addProduct";
    }


    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam("imageFile") MultipartFile file,
                             BindingResult result, Model model, HttpSession session) {
        if(!file.isEmpty()) {
           try {
               // 🌟 Lưu file ảnh vào thư mục static/images/
               String fileName = file.getOriginalFilename();
               Path filePath = Paths.get("src/main/resources/static/images/" + fileName);
               Files.write(filePath, file.getBytes());

               // 🌟 Lưu tên file ảnh vào product
                product.setImageUrl("/images/" + fileName);

           } catch (Exception e) {
               model.addAttribute("registrationError", "Thêm sản phẩm không thành công");
               return "/manage_product/addProduct";
           }
        }
        if (result.hasErrors()) {
            model.addAttribute("registrationError", "Thêm sản phẩm không thành công");
            return "/manage_product/addProduct";
        }
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {
            model.addAttribute("registrationError", "Thêm sản phẩm không thành công");
            return "/manage_product/addProduct";
        }
        product.setCreatedBy(user);

        productService.addProduct(product);
        return "redirect:/manageProduct";
    }

}
