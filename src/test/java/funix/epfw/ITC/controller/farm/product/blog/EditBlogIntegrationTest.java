package funix.epfw.ITC.controller.farm.product.blog;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EditBlogIntegrationTest {

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
    private Blog blog;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setRole(Role.FARMER);

        product = new Product();
        product.setId(10L);

        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Old title");
        blog.setContent("Old content");
        blog.setProducts(List.of(product));

        when(blogService.findById(1L)).thenReturn(blog);
    }

    @Test
    void testEditBlog_Get_Success() throws Exception {
        mockMvc.perform(get("/editBlog/product/1")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blog"))
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name(ViewPaths.EDIT_BLOG));
    }

    @Test
    void testEditBlog_Get_NotFound() throws Exception {
        when(blogService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/editBlog/product/999")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/farm/product/blog/manageBlog"));
    }

    @Test
    void testEditBlog_Post_Success() throws Exception {
        mockMvc.perform(post("/editBlog/product/1")
                        .param("title", "New title")
                        .param("content", "New content")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageBlog/product/10"));

        verify(blogService).saveBlog(argThat(updated ->
                updated.getTitle().equals("New title") &&
                        updated.getContent().equals("New content")));
    }

    @Test
    void testEditBlog_Post_NotFound() throws Exception {
        when(blogService.findById(999L)).thenReturn(null);

        mockMvc.perform(post("/editBlog/product/999")
                        .param("title", "Doesn't matter")
                        .param("content", "Still doesn't matter")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/farm/product/blog/manageBlog"));
    }
}