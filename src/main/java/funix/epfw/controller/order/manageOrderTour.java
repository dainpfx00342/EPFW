package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class manageOrderTour {
    private final OrderService orderService;

    @Autowired
    public manageOrderTour(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/manageOrderTour/{userId}")
    public String showManageOrderTour(@PathVariable("userId") Long userId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (!userId.equals(currentUser.getId())) {
            return "redirect:/accessDenied";
        }
        List<Order> orders = orderService.findOrdersTourByUser(userId);
        model.addAttribute("orders", orders);

    return  ViewPaths.MANAGE_ORDER_TOUR;
    }
}
