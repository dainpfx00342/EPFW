package funix.epfw.ITC.controller.farm.product.blog;

import funix.epfw.constants.Role;
import funix.epfw.model.farm.product.Blog;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteBlogIntegrationTest {

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
        user.setId(1L);
        user.setRole(Role.FARMER);

        Blog blog = new Blog();
        blog.setId(1L);

        when(blogService.findById(1L)).thenReturn(blog);
    }

    @Test
    void testDeleteBlog_Success() throws Exception {
        mockMvc.perform(get("/deleteBlog/1")
                        .param("productId", "10")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageBlog/10"));

        verify(blogService).deleteBlog(1L);
    }

    @Test
    void testDeleteBlog_NotFound() throws Exception {
        when(blogService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/deleteBlog/999")
                        .param("productId", "20")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageBlog/20"));

        verify(blogService, never()).deleteBlog(999L);
    }
}