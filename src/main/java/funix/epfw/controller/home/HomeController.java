package funix.epfw.controller.home;

import funix.epfw.model.product.Product;
import funix.epfw.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/","/home"})
    public String gohome(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "home/home";
    }

}
