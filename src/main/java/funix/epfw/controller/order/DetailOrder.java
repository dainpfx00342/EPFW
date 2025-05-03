package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailOrder {
    private final OrderService orderService;


    @Autowired
    public DetailOrder(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/detailOrder/{orderId}")
    public String detailOrder(@PathVariable("orderId") Long orderId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        Order order = orderService.findById(orderId);
        if(order == null) {
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy đơn hàng.");
            return "redirect:/order/manageOrderProduct";
        }
        model.addAttribute("order", order);

        return ViewPaths.DETAIL_ORDER;
    }
}
