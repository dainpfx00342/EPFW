package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.model.user.User;
import funix.epfw.model.vote.Comment;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.vote.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final BlogService blogService;

    @Autowired
    public CommentController(CommentService commentService, BlogService blogService) {
        this.commentService = commentService;
        this.blogService = blogService;
    }
    @PostMapping("/blog/{blogId}/comment")
    public String addComment(@PathVariable Long blogId, @ModelAttribute("comment") Comment comment, HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        comment.setBlog(blogService.findById(blogId));
        comment.setUser((User) session.getAttribute("loggedInUser"));
        commentService.save(comment);
        return "redirect:/blogDetail/" + blogId;
    }

}
