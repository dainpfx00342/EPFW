package funix.epfw.controller.farm.product;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.user.UserService;
import funix.epfw.service.farm.product.ProductService;
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
    private final FarmService farmService;

    @Autowired
    public ManageProduct(ProductService productService, UserService userService, FarmService farmService) {
        this.productService = productService;
        this.userService = userService;
        this.farmService = farmService;
    }

    @GetMapping("/manageProduct")
    public String manageProduct(HttpSession session, Model model) {


        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        currentUser = userService.findByUsername(currentUser.getUsername());
        List<Product> products;
        if(currentUser.getRole() == Role.ADMIN){
            products = productService.findAll();

        }else {
            List <Farm> farms = farmService.findByUserId(currentUser.getId());
            products = productService.findByFarms(farms);
        }
        for(Product product : products) {
            if(product.getNumberOfStock().equals("0")){
                product.setStatus(false);

            }
        }

        model.addAttribute("products", products);
        return ViewPaths.MANAGE_PRODUCT;

    }
}
