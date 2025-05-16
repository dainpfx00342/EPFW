package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DetailOrderUser {
    private final OrderService orderService;

    @Autowired
    public DetailOrderUser(OrderService orderService) {

        this.orderService = orderService;
    }

    @GetMapping("/detailOrderUser/{orderId}")
    public String detailOrderUser(@PathVariable("orderId") Long orderId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String checkAuth = AuthUtil.checkAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        if (orderService.findById(orderId) == null) {
            redirectAttributes.addFlashAttribute(Message.ERROR_MESS, "Không tìm thấy đơn hàng.");
            return "redirect:/manageOrderUser/"+currentUser.getId();
        }
        if(!orderService.checkOrderBelongsToUser(orderId, currentUser.getId()) && !orderService.checkOrderBelongsOwwer(orderId, currentUser.getId())) {
            redirectAttributes.addFlashAttribute(Message.ERROR_MESS, "Bạn không có quyền truy cập vào đơn hàng này.");
            return "redirect:/manageOrderUser/"+currentUser.getId();
        }

        model.addAttribute("order", orderService.findById(orderId));
        return ViewPaths.DETAIL_ORDER_USER;
    }
}
