package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.vote.Comment;
import funix.epfw.model.vote.Vote;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.vote.CommentService;
import funix.epfw.service.vote.VoteService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailBlogTest {

    @Mock
    private BlogService blogService;

    @Mock
    private VoteService voteService;

    @Mock
    private CommentService commentService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private DetailBlog detailBlog;

    private final Long blogId = 1L;

    @Test
    void test_blogDetail_AccessDenied() {
        try (MockedStatic<AuthUtil> mocked = Mockito.mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/accessDenied");

            String view = detailBlog.blogDetail(blogId, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_blogDetail_BlogNotFound() {
        try (MockedStatic<AuthUtil> mocked = Mockito.mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(blogService.findById(blogId)).thenReturn(null);

            String view = detailBlog.blogDetail(blogId, model, session);
            assertEquals(ViewPaths.HOME, view);
            verify(model).addAttribute(Message.ERROR_MESS, "Không tìm thấy blog để hiển thị.");
        }
    }

    @Test
    void test_blogDetail_WithProduct() {
        try (MockedStatic<AuthUtil> mocked = Mockito.mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Blog blog = new Blog();
            Product product = new Product();
            blog.setProducts(List.of(product));

            when(blogService.findById(blogId)).thenReturn(blog);
            when(voteService.findByBlogId(blogId)).thenReturn(new ArrayList<>());
            when(commentService.findByBlogAndParentNull(blog)).thenReturn(new ArrayList<>());

            String view = detailBlog.blogDetail(blogId, model, session);
            assertEquals(ViewPaths.DETAIL_BLOG, view);

            verify(model).addAttribute("productId", product.getId());
            verify(model).addAttribute("product", product);
            verify(model).addAttribute("comments", new ArrayList<>());
            verify(model).addAttribute("votes", new ArrayList<>());
            verify(model).addAttribute("blog", blog);
            verify(model).addAttribute("comment", new Comment());
            verify(model).addAttribute("reply", new Comment());
        }
    }

    @Test
    void test_blogDetail_WithTour() {
        try (MockedStatic<AuthUtil> mocked = Mockito.mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Blog blog = new Blog();
            Tour tour = new Tour();
            blog.setTours(List.of(tour));

            when(blogService.findById(blogId)).thenReturn(blog);
            when(voteService.findByBlogId(blogId)).thenReturn(new ArrayList<>());
            when(commentService.findByBlogAndParentNull(blog)).thenReturn(new ArrayList<>());

            String view = detailBlog.blogDetail(blogId, model, session);
            assertEquals(ViewPaths.DETAIL_BLOG, view);

            verify(model).addAttribute("tourId", tour.getId());
            verify(model).addAttribute("tour", tour);
            verify(model).addAttribute("comments", new ArrayList<>());
            verify(model).addAttribute("votes", new ArrayList<>());
            verify(model).addAttribute("blog", blog);
            verify(model).addAttribute("comment", new Comment());
            verify(model).addAttribute("reply", new Comment());
        }
    }

    @Test
    void test_blogDetail_Success() {
        try (MockedStatic<AuthUtil> mocked = Mockito.mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Blog blog = new Blog();
            Product product = new Product();
            product.setId(1L);
            blog.setProducts(List.of(product));

            List<Vote> votes = new ArrayList<>();
            List<Comment> comments = new ArrayList<>();

            when(blogService.findById(blogId)).thenReturn(blog);
            when(voteService.findByBlogId(blogId)).thenReturn(votes);
            when(commentService.findByBlogAndParentNull(blog)).thenReturn(comments);

            String view = detailBlog.blogDetail(blogId, model, session);
            assertEquals(ViewPaths.DETAIL_BLOG, view);

            verify(model).addAttribute("productId", product.getId());
            verify(model).addAttribute("product", product);
            verify(model).addAttribute("comments", comments);
            verify(model).addAttribute("votes", votes);
            verify(model).addAttribute("blog", blog);
            verify(model).addAttribute("comment", new Comment());
            verify(model).addAttribute("reply", new Comment());
        }
    }
}