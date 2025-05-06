package funix.epfw.ITC.controller.farm.product.blog;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.ProductService;
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

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddBlogProductIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }

        @Bean
        public BlogService blogService() {
            return Mockito.mock(BlogService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private BlogService blogService;

    private Product product;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setRole(Role.FARMER);

        product = new Product();
        product.setId(1L);
        product.setBlogs(new ArrayList<>());
        product.setFarm(new funix.epfw.model.farm.Farm());
        product.getFarm().setUser(user);

        when(productService.findById(1L)).thenReturn(product);
    }

    @Test
    void testShowAddBlogForm_Success() throws Exception {
        mockMvc.perform(get("/addBlog/product/1")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("product"))
                .andExpect(view().name(ViewPaths.ADD_BLOG));
    }

    @Test
    void testAddBlog_Success() throws Exception {
        mockMvc.perform(post("/addBlog/product/1")
                        .sessionAttr("loggedInUser", user)
                        .param("blogTitle", "Test Blog")
                        .param("content", "Nội dung blog test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageBlog/product/1"));

        verify(blogService).saveBlog(any(Blog.class));
    }

    @Test
    void testAddBlog_ProductNotFound() throws Exception {
        when(productService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/addBlog/product/999")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageProduct?error=productNotFound"));

    }
}