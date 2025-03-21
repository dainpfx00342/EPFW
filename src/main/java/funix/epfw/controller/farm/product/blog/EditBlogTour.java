package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.product.blog.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller()
public class EditBlogTour {
    private final BlogService blogService;


    @Autowired
    public EditBlogTour(BlogService blogService) {
        this.blogService = blogService;

    }

    @GetMapping("/editBlog/tour/{blogId}")
    public String editBlogTour(@PathVariable Long blogId,Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }
        Blog blog = blogService.findById(blogId);
        Tour tour = blog.getTours().getFirst();

        model.addAttribute("tour", tour);
        model.addAttribute("blog", blog);

        return ViewPaths.EDIT_BLOG_TOUR;
    }

    @PostMapping("/editBlog/tour/{blogId}")
    public String editBlogTour(@PathVariable Long blogId, Blog blog) {
        Blog blogToUpdate = blogService.findById(blogId);
        blogToUpdate.setTitle(blog.getTitle());
        blogToUpdate.setContent(blog.getContent());

        blogService.saveBlog(blogToUpdate);

        return "redirect:/manageBlog/tour/"+blogToUpdate.getTours().getFirst().getId();
    }
}
