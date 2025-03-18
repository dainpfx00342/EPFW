package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("loggedInUser")
public class AddOrder {
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public AddOrder(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;

    }

    @GetMapping("/addOrder/product/{productId}")
    public String addOrder(Model model, HttpSession session, @PathVariable Long productId) {

        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        Product currentProduct = productService.findById(productId);
        if(currentProduct == null) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy sản phẩm!?");
            return ViewPaths.ADD_ORDER;
        }
        model.addAttribute("product", currentProduct);
        model.addAttribute("order", new Order());
        return ViewPaths.ADD_ORDER;
    }

    @PostMapping("addOrder/product/{id}")
    public String addOrder(@PathVariable Long id,@Validated @ModelAttribute("order") Order newOrder,
                           BindingResult result,
                           Model model, HttpSession session) {

        String checkAuth = AuthUtil.checkBuyerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
            Product currentProduct = productService.findById(id);
            if(currentProduct == null) {
                model.addAttribute(Message.ERROR_MESS,"Không tìm thấy sản phẩm");
                return ViewPaths.ADD_ORDER;
            }
            newOrder.setProduct(currentProduct);

        if(result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS,"Vui lòng điền đầy đủ và chính xác thông tin liên hệ");
            model.addAttribute("order", newOrder);
            model.addAttribute("product", currentProduct);
            return ViewPaths.ADD_ORDER;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        if(currentUser == null) {
            return ViewPaths.LOGIN;
        }
        newOrder.setUser(currentUser);

        orderService.saveOrder(newOrder);

        return ViewPaths.HOME;
    }
}
