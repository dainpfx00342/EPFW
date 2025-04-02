package funix.epfw.controller.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.model.order.Order;
import funix.epfw.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    // Phương thức chung để xử lý việc từ chối đơn hàng
    private String cancelOrder(Long orderId, String reason, String orderType, @RequestHeader(value = "Referer", required = false) String referer) {
        Order order = orderService.findById(orderId);
        String note = "";

        // Thiết lập ghi chú tùy thuộc vào loại đơn hàng
        if ("product".equals(orderType)) {
            note = "Đơn hàng bị từ chối bởi người bán. Lý do: " + reason;
        } else if ("tour".equals(orderType)) {
            note = "Đơn đặt tour bị từ chối bởi chủ trang trại. Lý do: " + reason;
        } else if ("buyer".equals(orderType)) {
            note = "Đơn dặt bị từ chối bởi người mua. Lý do: " + reason;
        }

        // Cập nhật trạng thái đơn hàng và ghi chú
        order.setNote(note);
        order.setOrderStatus(OrderStatus.CANCELED);
        orderService.saveOrder(order);

        // Chuyển hướng về trang trước đó nếu có
        if (referer != null) {
            return "redirect:" + referer;
        }
        return "redirect:/home";
    }

    @GetMapping("/canceledOrder/product/{orderId}")
    public String cancelOrderProduct(@PathVariable Long orderId,
                                     @RequestParam("reason") String reason,
                                     @RequestHeader(value = "Referer", required = false) String referer, Model model) {
        return cancelOrder(orderId, reason, "product", referer);
    }

    @GetMapping("/canceledOrder/tour/{orderId}")
    public String cancelOrderTour(@PathVariable Long orderId,
                                  @RequestParam("reason") String reason,
                                  @RequestHeader(value = "Referer", required = false) String referer, Model model) {
        return cancelOrder(orderId, reason, "tour", referer);
    }

    @GetMapping("/canceledOrder/buyer/{orderId}")
    public String cancelOrder(@PathVariable Long orderId,
                              @RequestParam("reason") String reason,
                              @RequestHeader(value = "Referer", required = false) String referer, Model model) {
        return cancelOrder(orderId, reason, "buyer", referer);
    }
}
