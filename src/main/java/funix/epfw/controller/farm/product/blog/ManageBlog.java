package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.List;

@Controller
@SessionAttributes("loggedInUser")
public class ManageBlog {
    private final BlogService blogService;
    private final ProductService productService;
    private final TourService tourService;


    @Autowired
    public ManageBlog(BlogService blogService, ProductService productService, TourService tourService) {
        this.blogService = blogService;
        this.productService = productService;
        this.tourService = tourService;
    }


    @GetMapping("/manageBlog/product/{productId}")
    public String manageBlog(@PathVariable Long productId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }

        Product product = productService.findById(productId);
        if (product == null) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy sản phẩm.");
            return "redirect:/manageBlog/product/"+productId;
        }
        List<Blog> blogs = blogService.getBlogsByProductId(productId);
        if (blogs == null || blogs.isEmpty()) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy bài viết nào cho sản phẩm này.");
            model.addAttribute("product", product);
            return ViewPaths.MANAGE_BLOG;
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("product",product);
        model.addAttribute("productId",productId);
        return ViewPaths.MANAGE_BLOG;
    }
    @GetMapping("/manageBlog/tour/{tourId}")
    public String manageBlogTour(@PathVariable Long tourId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }
        Tour tour = tourService.findById(tourId);
        if (tour == null) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy chuyến tham quan.");
            return "redirect:/farm/product/blog/manageBlogTour";
        }
        List<Blog> blogs = blogService.getBlogsByTourId(tourId);
        if (blogs == null || blogs.isEmpty()) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy bài viết nào cho chuyến tham quan này.");
            return "redirect:/farm/product/blog/manageBlogTour";
        }
        model.addAttribute("blogs",blogs);
        model.addAttribute("tour",tour);
        model.addAttribute("tourId",tourId);
        return ViewPaths.MANAGE_BLOG_TOUR;
    }
}
