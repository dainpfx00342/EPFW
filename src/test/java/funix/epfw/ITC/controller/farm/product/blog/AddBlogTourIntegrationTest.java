package funix.epfw.ITC.controller.farm.product.blog;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.tour.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AddBlogTourIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public TourService tourService() {
            return Mockito.mock(TourService.class);
        }

        @Bean
        public BlogService blogService() {
            return Mockito.mock(BlogService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TourService tourService;

    @Autowired
    private BlogService blogService;

    private User user;
    private Tour tour;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setRole(Role.FARMER);

        tour = new Tour();
        tour.setId(1L);
        tour.setBlogs(new ArrayList<>());
        tour.setFarm(new funix.epfw.model.farm.Farm());
        tour.getFarm().setUser(user);

        when(tourService.findById(1L)).thenReturn(tour);
    }

    @Test
    void testShowAddBlogTourForm_Success() throws Exception {
        mockMvc.perform(get("/addBlog/tour/1")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("tour"))
                .andExpect(model().attributeExists("blog"))
                .andExpect(view().name(ViewPaths.ADD_BLOG_TOUR));
    }

    @Test
    void testSaveBlog_Success() throws Exception {
        mockMvc.perform(post("/addBlog/tour/1")
                        .sessionAttr("loggedInUser", user)
                        .param("blogTitle", "Blog Tour Test")
                        .param("content", "Nội dung blog tour"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageBlog/tour/1"));

        verify(blogService).saveBlog(any(Blog.class));
    }

    @Test
    void testSaveBlog_TourNotFound() throws Exception {
        when(tourService.findById(999L)).thenReturn(null);

        mockMvc.perform(post("/addBlog/tour/999")
                        .sessionAttr("loggedInUser", user)
                        .param("blogTitle", "Blog Test")
                        .param("content", "Nội dung"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageTour?error=tourNotFound"));
    }
}