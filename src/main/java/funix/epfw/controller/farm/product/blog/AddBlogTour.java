package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class AddBlogTour {
    private final TourService tourService;
    private final BlogService blogService;

    public AddBlogTour(TourService tourService, BlogService blogService) {
        this.tourService = tourService;
        this.blogService = blogService;
    }

    @GetMapping("/addBlog/tour/{tourId}")
    public String addBlog (@PathVariable Long tourId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }

        model.addAttribute("tour", tourService.findById(tourId));
        model.addAttribute("blog", new Blog());
        return ViewPaths.ADD_BLOG_TOUR;
    }

    @PostMapping("/addBlog/tour/{tourId}")
    public String saveBlog (@PathVariable Long tourId, Blog blog, Model model,HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }
        Tour tour = tourService.findById(tourId);
        if(tour==null){
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy chuyến tham quan.");
            return ViewPaths.ADD_BLOG_TOUR;
        }

        if(blog.getTours()==null){
            blog.setTours(new ArrayList<>());
        }

        blog.getTours().add(tour);
        tour.getBlogs().add(blog);
        blogService.saveBlog(blog);
        return "redirect:/manageBlog/tour/"+tourId;
    }
}
