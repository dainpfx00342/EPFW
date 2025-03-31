package funix.epfw.controller.order;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.order.Order;
import funix.epfw.model.vote.Vote;
import funix.epfw.service.order.OrderService;
import funix.epfw.service.vote.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewOrder {
    private final OrderService orderService;
    private final VoteService voteService;

    @Autowired
    public ReviewOrder(OrderService orderService, VoteService voteService) {
        this.orderService = orderService;
        this.voteService = voteService;
    }

    @GetMapping("/reviewOrder/{orderId}")
    public String reviewOrder(@PathVariable Long orderId, Model model) {
        model.addAttribute("order", orderService.findById(orderId));
        model.addAttribute("vote",new Vote());
        return ViewPaths.REVIEW_ORDER;
    }

    @PostMapping("/reviewOrder/{orderId}")
    public String reviewOrder(@PathVariable Long orderId, @ModelAttribute("review") Vote vote, Model model) {
        Order order = orderService.findById(orderId);
        Blog  orderBlog = order.getBlog();

        vote.setBlog(orderBlog);
        vote.setOrder(order);
        voteService.saveVote(vote);
        return ViewPaths.MANAGE_ORDER_USER;

    }
}
