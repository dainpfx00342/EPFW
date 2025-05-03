package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageBlogTest {

    @Mock
    private BlogService blogService;

    @Mock
    private ProductService productService;

    @Mock
    private TourService tourService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private ManageBlog manageBlog;

    private final Long id = 1L;

    // ========== TEST PRODUCT ==========

    @Test
    void manageBlog_accessDenied() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String result = manageBlog.manageBlog(id, model, session);
            assertEquals("redirect:/accessDenied", result);
        }
    }

    @Test
    void manageBlog_productNotFound() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(productService.findById(id)).thenReturn(null);

            String result = manageBlog.manageBlog(id, model, session);
            assertEquals("redirect:/farm/product/blog/manageBlog", result);
            verify(model).addAttribute(eq(Message.ERROR_MESS), contains("Không tìm thấy sản phẩm"));
        }
    }

    @Test
    void manageBlog_noBlogsFound() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(productService.findById(id)).thenReturn(new Product());
            when(blogService.getBlogsByProductId(id)).thenReturn(Collections.emptyList());

            String result = manageBlog.manageBlog(id, model, session);
            assertEquals("redirect:/farm/product/blog/manageBlog", result);
            verify(model).addAttribute(eq(Message.ERROR_MESS), contains("Không tìm thấy bài viết"));
        }
    }

    @Test
    void manageBlog_success() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            Product product = new Product();
            List<Blog> blogs = List.of(new Blog());

            when(productService.findById(id)).thenReturn(product);
            when(blogService.getBlogsByProductId(id)).thenReturn(blogs);

            String result = manageBlog.manageBlog(id, model, session);
            assertEquals(ViewPaths.MANAGE_BLOG, result);
            verify(model).addAttribute("blogs", blogs);
            verify(model).addAttribute("product", product);
            verify(model).addAttribute("productId", id);
        }
    }

    // ========== TEST TOUR ==========

    @Test
    void manageBlogTour_accessDenied() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String result = manageBlog.manageBlogTour(id, model, session);
            assertEquals("redirect:/accessDenied", result);
        }
    }

    @Test
    void manageBlogTour_tourNotFound() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(tourService.findById(id)).thenReturn(null);

            String result = manageBlog.manageBlogTour(id, model, session);
            assertEquals("redirect:/farm/product/blog/manageBlogTour", result);
            verify(model).addAttribute(eq(Message.ERROR_MESS), contains("Không tìm thấy chuyến tham quan"));
        }
    }

    @Test
    void manageBlogTour_noBlogsFound() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(tourService.findById(id)).thenReturn(new Tour());
            when(blogService.getBlogsByTourId(id)).thenReturn(Collections.emptyList());

            String result = manageBlog.manageBlogTour(id, model, session);
            assertEquals("redirect:/farm/product/blog/manageBlogTour", result);
            verify(model).addAttribute(eq(Message.ERROR_MESS), contains("Không tìm thấy bài viết"));
        }
    }

    @Test
    void manageBlogTour_success() {
        try (MockedStatic<AuthUtil> mockAuth = Mockito.mockStatic(AuthUtil.class)) {
            mockAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            Tour tour = new Tour();
            List<Blog> blogs = List.of(new Blog());

            when(tourService.findById(id)).thenReturn(tour);
            when(blogService.getBlogsByTourId(id)).thenReturn(blogs);

            String result = manageBlog.manageBlogTour(id, model, session);
            assertEquals(ViewPaths.MANAGE_BLOG_TOUR, result);
            verify(model).addAttribute("blogs", blogs);
            verify(model).addAttribute("tour", tour);
            verify(model).addAttribute("tourId", id);
        }
    }
}