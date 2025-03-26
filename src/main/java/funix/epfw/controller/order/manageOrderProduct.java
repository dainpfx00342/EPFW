package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class manageOrderProduct {
    private final OrderService orderService;

    @Autowired
    public manageOrderProduct(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/manageOrderProduct/{userId}")
    public String showManageOrderUser(@PathVariable Long userId, Model model,  HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth!=null) {
            return checkAuth;
        }

        List<Order> orders = orderService.findOrdersProductByUser(userId);
        model.addAttribute("orders", orders);


        return ViewPaths.MANAGE_ORDER_PRODUCT;
    }


}
