package funix.epfw.controller.order;

import funix.epfw.constants.Message;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConfirmOrder {

    private final OrderService orderService;

    @Autowired
    public ConfirmOrder(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/confirmOrder/{orderId}")
    public String confirmOrderProduct(@PathVariable("orderId") Long orderId, RedirectAttributes redirectAttributes, HttpSession session) {
        String checkAuth = (String) session.getAttribute("checkAuth");
        if(checkAuth != null) {
            return checkAuth;
        }

        orderService.confirmOrder(orderId);
        User currentUser = (User) session.getAttribute("loggedInUser");
        Long userId = currentUser.getId();
        orderService.confirmOrder(orderId);
        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS, "Xác nhận đơn hàng thành công");
        return "redirect:/manageOrderProduct/"+userId;
    }

//    @GetMapping("/confirmOrder/tour/{orderId}")
//    public String confirmOrderTour(@PathVariable("orderId") Long orderId, RedirectAttributes redirectAttributes, HttpSession session) {
//        String checkAuth = (String) session.getAttribute("checkAuth");
//        if(checkAuth != null) {
//            return checkAuth;
//        }
//        orderService.confirmOrder(orderId);
//        User currentUser = (User) session.getAttribute("loggedInUser");
//        Long userId = currentUser.getId();
//        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS, "Xác nhận đơn hàng: "+orderId+" thành công");
//        return "redirect:/manageOrderTour/"+userId;
//    }
}
