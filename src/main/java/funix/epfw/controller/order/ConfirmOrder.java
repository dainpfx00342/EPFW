package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.model.order.Order;
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
    public String confirmOrderProduct(@PathVariable("orderId") Long orderId,
                                      RedirectAttributes redirectAttributes, HttpSession session) {

        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        Long currentUserId = currentUser.getId();
        Order currentOrder = orderService.findById(orderId);
        if(currentOrder != null) {

            boolean isOwner = currentOrder.getProducts().stream()
                    .anyMatch(product -> product.getFarm().getUser().getId().equals(currentUserId)) ||
                    currentOrder.getTours().stream()
                            .anyMatch(tour -> tour.getFarm().getUser().getId().equals(currentUserId));
            if (!isOwner) {

                return "redirect:/accessDenied";
            }

            orderService.confirmOrder(orderId);  // Chỉ gọi 1 lần duy nhất
            String redirectUrl = currentOrder.getProducts().isEmpty() ? "/manageOrderTour/" : "/manageOrderProduct/";
            redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS, "Xác nhận đơn hàng thành công");
            return "redirect:" + redirectUrl + currentUserId;
        }
        return "redirect:/manageOrderProduct/" + currentUserId;

    }
}
