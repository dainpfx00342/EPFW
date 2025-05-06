package funix.epfw.ITC.controller.farm.product.blog;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.blog.BlogService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EditBlogTourIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public BlogService blogService() {
            return Mockito.mock(BlogService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogService blogService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(2L);
        user.setRole(Role.FARMER);

        Tour tour = new Tour();
        tour.setId(20L);

        Blog blog = new Blog();
        blog.setId(2L);
        blog.setTitle("Old title tour");
        blog.setContent("Old content tour");
        blog.setTours(List.of(tour));

        when(blogService.findById(2L)).thenReturn(blog);
    }

    @Test
    void testEditBlogTour_Get_Success() throws Exception {
        mockMvc.perform(get("/editBlog/tour/2")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blog"))
                .andExpect(model().attributeExists("tour"))
                .andExpect(view().name(ViewPaths.EDIT_BLOG_TOUR));
    }

    @Test
    void testEditBlogTour_Get_NotFound() throws Exception {
        when(blogService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/editBlog/tour/999")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"+ViewPaths.MANAGE_BLOG_TOUR));
    }

    @Test
    void testEditBlogTour_Post_Success() throws Exception {
        mockMvc.perform(post("/editBlog/tour/2")
                        .param("title", "New tour title")
                        .param("content", "New tour content")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageBlog/tour/20"));

        verify(blogService).saveBlog(argThat(updated ->
                updated.getTitle().equals("New tour title") &&
                        updated.getContent().equals("New tour content")));
    }

    @Test
    void testEditBlogTour_Post_NotFound() throws Exception {
        when(blogService.findById(999L)).thenReturn(null);

        mockMvc.perform(post("/editBlog/tour/999")
                        .param("title", "Không quan trọng")
                        .param("content", "Vẫn không quan trọng")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"+ViewPaths.MANAGE_BLOG_TOUR));
    }
}