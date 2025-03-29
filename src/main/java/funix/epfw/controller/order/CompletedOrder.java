package funix.epfw.controller.order;

import funix.epfw.constants.Message;
import funix.epfw.model.order.Order;
import funix.epfw.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String completedOrder(@PathVariable("orderId") Long orderId, RedirectAttributes redirectAttributes) {

        orderService.completeOrder(orderId);
        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS, "Đã hoàn thành đơn hàng: "+ orderId);
        return "redirect:/manageOrderUser/"+orderService.findById(orderId).getUser().getId();
    }
}
