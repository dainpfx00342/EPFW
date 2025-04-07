package funix.epfw.controller.report;

import funix.epfw.constants.AuthUtil;
import funix.epfw.model.order.Order;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class OrderReport {
    private final OrderService orderService;

    @Autowired
    public OrderReport(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orderReport/{userId}")
    public String showOrderReport(@PathVariable Long userId, Model model, HttpSession session) {
        // Kiểm tra quyền truy cập
      String checkAuth = AuthUtil.checkFarmerAuth(session);
      if(checkAuth != null) {
          return checkAuth;
      }
        // Lấy danh sách đơn hàng của người dùng
      List<Order> orderProducts = orderService.findOrdersProductByUser(userId);
      List<Order> orderTours = orderService.findOrdersTourByUser(userId);
        // Thêm danh sách đơn hàng vào mô hình
        model.addAttribute("orderProdcut", orderProducts);
        model.addAttribute("orderTour", orderTours);

       return "/home/home";
    }
}
