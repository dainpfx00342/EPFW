package funix.epfw.controller.manageProduct;

import funix.epfw.constants.Role;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FramerAuth;
import funix.epfw.model.product.Product;
import funix.epfw.model.User;
import funix.epfw.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("loggedInUser")
public class ManageProduct {
    private final ProductService productService;

    @Autowired
    public ManageProduct(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/manageProduct")
    public String manageProduct(HttpSession session, Model model) {
        AuthChecker authoChecker = new FramerAuth();
        String accessCheck = authoChecker.checkAuth(session);
        if(accessCheck != null) {
            return accessCheck;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        if(currentUser.getRole()!= Role.ADMIN) {
            List<Product> products = productService.getAllProductsByUser(currentUser);
            model.addAttribute("products", products);
            return "/manage_product/manageProduct";
        }
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "/manage_product/manageProduct";

    }
}
