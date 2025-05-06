package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.tour.TourService;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller

public class AddOrderTour {
    private final OrderService orderService;
    private final TourService tourService;
    private final BlogService blogService;

    @Autowired
    public AddOrderTour(OrderService orderService, TourService tourService, BlogService blogService) {
        this.orderService = orderService;
        this.tourService = tourService;
        this.blogService = blogService;
    }

    @GetMapping("/addOrder/tour/{tourId}")
    public String addOrderTour(@PathVariable Long tourId, @RequestParam("blogId") Long blogId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkBuyerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }
        Tour tour = tourService.findById(tourId);
        if(tour == null){
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy tour");
            return "redirect:/home?error=tourNotFound";
        }
        Blog blog = blogService.findById(blogId);
        if(blog == null){
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy blog");
            return "redirect:/home?error=blogNotFound";
        }
        model.addAttribute("tour", tour);
        model.addAttribute("order", new Order());
        model.addAttribute("blog", blog);

        return ViewPaths.ADD_ORDER_TOUR;
    }

    @PostMapping("/addOrder/tour/{tourId}")
    public String saveOrderTour(@PathVariable Long tourId, @RequestParam("blogId") Long blogId,
                                @Validated @ModelAttribute("order") Order newOrderTour,
                                BindingResult result,
                                Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkBuyerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }

        Tour tour = tourService.findById(tourId);
        if(tour== null){
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy tour");
            return "redirect:/home?error=tourNotFound";
        }
        Blog blog = blogService.findById(blogId);
        if(blog==null){
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy blog");
            return "redirect:/home?error=blogNotFound";
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if(loggedInUser==null){
            return "redirect:/home?error=notLoggedIn";
        }
        if(result.hasErrors()){
            model.addAttribute(Message.ERROR_MESS,"Vui lòng nhập đầy đủ thông tin");
            model.addAttribute("order", newOrderTour);
            model.addAttribute("tour", tour);
            model.addAttribute("blog", blog);
            return ViewPaths.ADD_ORDER_TOUR;
        }

        if(newOrderTour.getExpectedPrice()==0){
            newOrderTour.setExpectedPrice(tour.getTicketPrice());
        }
        // Gan thoong tin vao don hang
        newOrderTour.setUser(loggedInUser);
        if(newOrderTour.getTours()==null){
            newOrderTour.setTours(new ArrayList<>());
        }
        newOrderTour.setBlog(blog);
        newOrderTour.getTours().add(tour);
        tour.getOrders().add(newOrderTour);
        newOrderTour.setOrderType("TOUR");
        try{
            orderService.saveOrder(newOrderTour);
        }catch(Exception e){
            model.addAttribute(Message.ERROR_MESS,"Lỗi hệ thống vui lòng thử lại");
            return ViewPaths.ADD_ORDER_TOUR;
        }

        return "redirect:/home";
    }
}
