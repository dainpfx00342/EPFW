package funix.epfw.controller.order;

import funix.epfw.constants.ViewPaths;
import funix.epfw.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailOrderUser {
    private final OrderService orderService;

    @Autowired
    public DetailOrderUser(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/detailOrderUser/{orderId}")
    public String detailOrderUser(@PathVariable("orderId") Long orderId, Model model) {
        model.addAttribute("order", orderService.findById(orderId));
        return ViewPaths.DETAIL_ORDER_USER;
    }
}
