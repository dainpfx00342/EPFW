package funix.epfw.controller.home;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.OrderStatus;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class HeaderController {
    private final OrderService orderService;

    @Autowired
    public HeaderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ModelAttribute("pendingOrderCount")
    public int getPendingOrderCount(HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);

        if (checkAuth != null) {
            return 0;
        }
        return orderService.countByOrderStatus(OrderStatus.PENDING);
    }

    @ModelAttribute("pendingOrderProductCount")
    public int getPendingOrderProductCount(HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);

        if (checkAuth != null) {
            return 0;
        }

        // Đếm đơn hàng sản phẩm có trạng thái PENDING
        int countProductOrders = orderService.countOrderProductStatus(OrderStatus.PENDING);

        System.out.println("DEBUG: Số lượng đơn hàng sản phẩm PENDING = " + countProductOrders);

        return countProductOrders;
    }

    @ModelAttribute("pendingOrderTourCount")
    public int getPendingOrderTourCount(HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if (checkAuth != null) {
            return 0;
        }
        int countTourOrders = orderService.countOrderTourStatus(OrderStatus.PENDING);
        System.out.println("DEBUG: Số lượng đơn hàng tour PENDING = " + countTourOrders);
        return orderService.countOrderTourStatus(OrderStatus.PENDING);
    }

}
