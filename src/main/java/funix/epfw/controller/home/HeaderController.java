package funix.epfw.controller.home;

import funix.epfw.constants.AuthUtil;
import funix.epfw.model.user.User;
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
        Long userId = ((User) session.getAttribute("loggedInUser")).getId();
        return orderService.countByOrderStatus(OrderStatus.PENDING, userId);
    }

    @ModelAttribute("pendingOrderProductCount")
    public int getPendingOrderProductCount(HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);

        if (checkAuth != null) {
            return 0;
        }

        Long userId = ((User) session.getAttribute("loggedInUser")).getId();

        // Đếm đơn hàng sản phẩm có trạng thái PENDING
        return orderService.countOrderProductStatus(OrderStatus.PENDING, userId);
    }

    @ModelAttribute("pendingOrderTourCount")
    public int getPendingOrderTourCount(HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if (checkAuth != null) {
            return 0;
        }
        Long userId = ((User) session.getAttribute("loggedInUser")).getId();
        // Đếm đơn hàng sản phẩm có trạng thái PENDING
        return orderService.countOrderTourStatus(userId, OrderStatus.PENDING);
    }

    @ModelAttribute("pendingOrderUserCount")
    public int getPendingOrderUserCount(HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if (checkAuth != null) {
            return 0;
        }
        Long userId = ((User) session.getAttribute("loggedInUser")).getId();
        // Đếm đơn hàng sản phẩm có trạng thái PENDING
        return orderService.countOrderUserStatus(userId, OrderStatus.PENDING);
    }
}
