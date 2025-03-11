package funix.epfw.controller.farm.product;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FarmerAuth;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.UserService;
import funix.epfw.service.productService.ProductService;
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
    private final UserService userService;

    @Autowired
    public ManageProduct(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/manageProduct")
    public String manageProduct(HttpSession session, Model model) {
        AuthChecker authoChecker = new FarmerAuth();
        String accessCheck = authoChecker.checkAuth(session);
        if(accessCheck != null) {
            return accessCheck;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        currentUser = userService.findByUsername(currentUser.getUsername());
        List<Product> products;
        if(currentUser.getRole() == Role.ADMIN){
            products = productService.findAll();

        }else {
            List <Farm> farms = currentUser.getFarms();
            products = productService.findByFarms(farms);
        }

        model.addAttribute("products", products);
        return ViewPaths.MANAGE_PRODUCT;

    }
}
