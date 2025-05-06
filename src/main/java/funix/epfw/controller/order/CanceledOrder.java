package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.OrderStatus;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CanceledOrder {
    private final OrderService orderService;

    @Autowired
    public CanceledOrder(OrderService orderService) {

        this.orderService = orderService;
    }


     String cancelOrder(Long orderId, String reason, String orderType,
                               @RequestHeader(value = "Referer", required = false) String referer, HttpSession session) {
        Order order = orderService.findById(orderId);
        if (order == null) {
            return "redirect:" + referer;
        }
        String checkAuth = AuthUtil.checkAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");


        boolean hasPermission = false;
        if ("product".equals(orderType)) {

            hasPermission = !order.getProducts().isEmpty() &&
                    order.getProducts().getFirst().getFarm().getUser().equals(currentUser);
        } else if ("tour".equals(orderType)) {

            hasPermission = !order.getTours().isEmpty() &&order.getTours().getFirst().getFarm().getUser().equals(currentUser);
        }else {
            hasPermission = order.getUser().equals(currentUser);
        }



        if (!hasPermission) {
            return "redirect:" + referer;
        }
        String note = switch (orderType){
            case "product" -> "Đơn hàng bị từ chối bởi người bán. Lý do: " + reason;
            case "tour" -> "Đơn đặt tour bị từ chối bởi chủ trang trại. Lý do: "+reason;
            case "buyer" -> "Đơn dặt bị từ chối bởi người mua. Lý do: "+reason;
            default -> "";
        };

        order.setNote(note);
        order.setOrderStatus(OrderStatus.CANCELED);
        orderService.saveOrder(order);

        return "redirect:" +referer;
    }

    @GetMapping("/canceledOrder/product/{orderId}")
    public String cancelOrderProduct(@PathVariable Long orderId,
                                     @RequestParam("reason") String reason,
                                     @RequestHeader(value = "Referer", required = false) String referer, HttpSession session) {
        return cancelOrder(orderId, reason, "product", referer, session);
    }

    @GetMapping("/canceledOrder/tour/{orderId}")
    public String cancelOrderTour(@PathVariable Long orderId,
                                  @RequestParam("reason") String reason,
                                  @RequestHeader(value = "Referer", required = false) String referer, HttpSession session) {
        return cancelOrder(orderId, reason, "tour", referer, session);
    }

    @GetMapping("/canceledOrder/buyer/{orderId}")
    public String cancelOrder(@PathVariable Long orderId,
                              @RequestParam("reason") String reason,
                              @RequestHeader(value = "Referer", required = false) String referer, HttpSession session) {
        return cancelOrder(orderId, reason, "buyer", referer, session);
    }
}
