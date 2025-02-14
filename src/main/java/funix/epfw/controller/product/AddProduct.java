package funix.epfw.controller.product;

import funix.epfw.constants.ROLE;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

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
                             BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("registrationError", "Thêm sản phẩm không thành công");
            return "/manage_product/addProduct";
        }
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null || user.getRole() != ROLE.FARMER) {
            model.addAttribute("registrationError", "Thêm sản phẩm không thành công");
            return "/manage_product/addProduct";
        }
        product.setCreatedBy(user);
        productService.addProduct(product);
        return "redirect:/manageProduct";
    }

}
