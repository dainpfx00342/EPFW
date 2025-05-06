package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.ui.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  AddBlogTourTest {

    @Mock
    private TourService tourService;

    @Mock
    private BlogService blogService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AddBlogTour addBlogTour;

    private final Long tourId = 1L;

    public AddBlogTourTest() {
        MockitoAnnotations.openMocks(this);
        addBlogTour = new AddBlogTour(tourService, blogService);
    }

    @Test
    void test_addBlog_accessDenied() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addBlogTour.addBlog(tourId, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_addBlog_success() {
        Tour tour = new Tour();

        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(tourService.findById(tourId)).thenReturn(tour);

            String view = addBlogTour.addBlog(tourId, model, session);
            assertEquals(ViewPaths.ADD_BLOG_TOUR, view);

            verify(model).addAttribute("tour", tour);
            verify(model).addAttribute(eq("blog"), any(Blog.class));
        }
    }

    @Test
    void test_saveBlog_tourNotFound() {
        Blog blog = new Blog();
        when(tourService.findById(tourId)).thenReturn(null);

        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            String view = addBlogTour.saveBlog(tourId, blog, model, session);
            assertEquals("redirect:/manageTour?error=tourNotFound", view);

        }
    }

    @Test
    void test_saveBlog_success() {
        Blog blog = new Blog();
        Tour tour = new Tour();
        tour.setBlogs(new ArrayList<>());

        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(tourService.findById(tourId)).thenReturn(tour);

            String view = addBlogTour.saveBlog(tourId, blog, model, session);
            assertEquals("redirect:/manageBlog/tour/" + tourId, view);

            // Kiểm tra mối quan hệ 2 chiều
            assertEquals(1, blog.getTours().size());
            assertEquals(tour, blog.getTours().getFirst());
            assertEquals(1, tour.getBlogs().size());
            assertEquals(blog, tour.getBlogs().getFirst());

            verify(blogService).saveBlog(blog);
        }
    }
}