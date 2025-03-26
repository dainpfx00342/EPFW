package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.repository.order.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class manageUserOrder {
    private final OrderRepository orderRepository;

    @Autowired
    public manageUserOrder(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/manageOrderUser/{userId}")
    public String showManageUserOrder(@PathVariable Long userId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        String checkAuth = AuthUtil.checkBuyerAuth(session);
        if(checkAuth !=null){
            return checkAuth;
        }

        List<Order> orders = orderRepository.findOrderByUserId(userId);
        model.addAttribute("orders", orders);
        model.addAttribute("user", user);
        return ViewPaths.MANAGE_ORDER_USER;
    }
}
