package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CompletedOrder {
    private final OrderService orderService;

    @Autowired
    public CompletedOrder(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/completedOrder/{orderId}")
    public String completedOrder(@PathVariable("orderId") Long orderId,
                                 RedirectAttributes redirectAttributes, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        orderService.completeOrder(orderId);

        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS, "Đã hoàn thành đơn hàng: "+ orderId);
        return "redirect:/manageOrderUser/"+orderService.findById(orderId).getUser().getId();
    }
}
