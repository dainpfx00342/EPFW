package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.product.blog.BlogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class DetailBlog {

    private final BlogService blogService;

    @Autowired
    public DetailBlog(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blogDetail/{blogId}")
    public String blogDetail(@PathVariable Long blogId, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if(checkAuth!=null){
            return checkAuth;
        }
        Blog blog = blogService.findById(blogId);
        if(blog!=null){

            if(blog.getProducts()!=null&&!blog.getProducts().isEmpty()){
                Product product = blog.getProducts().getFirst();
                model.addAttribute("productId", product.getId());

            }else if(blog.getTours()!=null&&!blog.getTours().isEmpty()){
                Tour tour = blog.getTours().getFirst();
                model.addAttribute("tourId", tour.getId());
            }
        }
        model.addAttribute("blog", blog);


        return ViewPaths.DETAIL_BLOG;
    }

}
