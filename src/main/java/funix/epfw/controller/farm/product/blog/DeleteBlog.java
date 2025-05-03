package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.service.farm.product.blog.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DeleteBlog {
    private final BlogService blogService;

    @Autowired
    public DeleteBlog(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable Long id,
                             @RequestParam("productId") Long productId,
                             HttpSession session, Model model) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        Blog blog = blogService.findById(id);
        if (blog == null) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy blog cần xóa hoặc blog không tồn tại");
            return "redirect:/manageBlog/"+productId;
        }

        blogService.deleteBlog(id);
        return "redirect:/manageBlog/"+productId;
    }



}
