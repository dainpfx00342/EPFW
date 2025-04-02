package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.vote.Vote;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.vote.VoteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
public class DetailBlog {

    private final BlogService blogService;
    private final VoteService voteService;

    @Autowired
    public DetailBlog(BlogService blogService, VoteService voteService) {
        this.blogService = blogService;
        this.voteService = voteService;
    }

    @GetMapping("/blogDetail/{blogId}")
    public String blogDetail(@PathVariable Long blogId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        Blog blog = blogService.findById(blogId);
        List<Vote> votes = null;
        if (blog != null) {
            votes = voteService.findByBlogId(blogId);
            if (blog.getProducts() != null && !blog.getProducts().isEmpty()) {
                Product product = blog.getProducts().getFirst();
                model.addAttribute("productId", product.getId());

            } else if (blog.getTours() != null && !blog.getTours().isEmpty()) {
                Tour tour = blog.getTours().getFirst();
                model.addAttribute("tourId", tour.getId());
            }
        }

        model.addAttribute("votes", votes);
        model.addAttribute("blog", blog);


        return ViewPaths.DETAIL_BLOG;
    }

}
