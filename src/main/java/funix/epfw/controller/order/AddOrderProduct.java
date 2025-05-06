package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.OrderStatus;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.order.OrderService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller

@Slf4j // Giúp log lỗi dễ hơn
public class AddOrderProduct {
    private final ProductService productService;
    private final OrderService orderService;
    private final BlogService blogService ;

    @Autowired
    public AddOrderProduct(OrderService orderService, ProductService productService, BlogService blogService) {
        this.orderService = orderService;
        this.productService = productService;
        this.blogService = blogService;

    }

    @GetMapping("/addOrder/product/{productId}")
    public String addOrder(@PathVariable Long productId,@RequestParam("blogId") Long blogId, Model model, HttpSession session) {

        String checkAuth = AuthUtil.checkBuyerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        Product currentProduct = productService.findById(productId);
        if(currentProduct == null) {

            return "redirect:/home?error=productNotFound";
        }
        Blog currentBlog = blogService.findById(blogId);
        if(currentBlog == null) {

            return "redirect:/home?error=blogNotFound";
        }



        model.addAttribute("product", currentProduct);
        model.addAttribute("order", new Order());
        model.addAttribute("blog", currentBlog);
        return ViewPaths.ADD_ORDER;
    }

    // Xử lý đặt hàng
    @PostMapping("/addOrder/product/{productId}")
    public String processOrder(@PathVariable Long productId, @RequestParam("blogId") Long blogId,
                               @Validated @ModelAttribute("order") Order newOrder,
                               BindingResult result,
                               Model model, HttpSession session) {

        String checkAuth = AuthUtil.checkBuyerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User user = (User) session.getAttribute("loggedInUser");
        if(user == null) {

            return "redirect:/home?error=notLoggedIn";
        }

        Product product = productService.findById(productId);
        if (product == null) {

            return "redirect:/home?error=productNotFound";
        }
        if (result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Vui lòng nhập đầy đủ dữ liệu!");
            model.addAttribute("order", newOrder);
            model.addAttribute("product", product);
            model.addAttribute("blog", blogService.findById(blogId));
            return ViewPaths.ADD_ORDER;
        }
        // Gán thông tin vào đơn hàng
        newOrder.setUser(user);
        if (newOrder.getProducts() == null) {
            newOrder.setProducts(new ArrayList<>());
        }
        newOrder.getProducts().add(product);
        newOrder.setBlog(blogService.findById(blogId));
        product.getOrders().add(newOrder);
        newOrder.setOrderType("PRODUCT");
        newOrder.setOrderStatus(OrderStatus.PENDING);
        if(newOrder.getExpectedPrice()==0){
            newOrder.setExpectedPrice(product.getPrice());
        }


        try {
            orderService.saveOrder(newOrder);
        } catch (Exception e) {
            log.error("Lỗi khi lưu đơn hàng: ", e);
            model.addAttribute(Message.ERROR_MESS, "Lỗi hệ thống! Vui lòng thử lại sau.");
            return ViewPaths.ADD_ORDER;
        }

        return "redirect:/home";
    }
}
