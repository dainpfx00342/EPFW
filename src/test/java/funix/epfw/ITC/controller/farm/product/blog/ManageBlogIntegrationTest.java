package funix.epfw.ITC.controller.farm.product.blog;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.ProductService;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManageBlogIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public BlogService blogService() {
            return Mockito.mock(BlogService.class);
        }

        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }

        @Bean
        public TourService tourService() {
            return Mockito.mock(TourService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ProductService productService;

    @Autowired
    private TourService tourService;

    private User user;
    private Product product;
    private Tour tour;
    private Blog blog;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setRole(Role.FARMER);

        Farm farm = new Farm();
        farm.setId(1L);
        farm.setFarmName("Farm 1");


        product = new Product();
        product.setId(10L);
        product.setProductName("Rau cải");
        product.setFarm(farm);

        tour = new Tour();
        tour.setId(20L);
        tour.setTourName("Tham quan vườn rau");
        tour.setFarm(farm);

        blog = new Blog();
        blog.setId(100L);
        blog.setTitle("Blog giới thiệu sản phẩm");
    }

    // PRODUCT

    @Test
    void testManageBlogProduct_Success() throws Exception {
        when(productService.findById(10L)).thenReturn(product);
        when(blogService.getBlogsByProductId(10L)).thenReturn(List.of(blog));

        mockMvc.perform(get("/manageBlog/product/10")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blogs"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("productId", 10L))
                .andExpect(view().name(ViewPaths.MANAGE_BLOG));
    }

    @Test
    void testManageBlogProduct_NotFound() throws Exception {
        when(productService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/manageBlog/product/999")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"+ViewPaths.MANAGE_BLOG));
    }

    @Test
    void testManageBlogProduct_NoBlogs() throws Exception {
        when(productService.findById(10L)).thenReturn(product);
        when(blogService.getBlogsByProductId(10L)).thenReturn(List.of());

        mockMvc.perform(get("/manageBlog/product/10")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"+ViewPaths.MANAGE_BLOG));
    }

    // TOUR

    @Test
    void testManageBlogTour_Success() throws Exception {
        when(tourService.findById(20L)).thenReturn(tour);
        when(blogService.getBlogsByTourId(20L)).thenReturn(List.of(blog));

        mockMvc.perform(get("/manageBlog/tour/20")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("blogs"))
                .andExpect(model().attributeExists("tour"))
                .andExpect(model().attribute("tourId", 20L))
                .andExpect(view().name(ViewPaths.MANAGE_BLOG_TOUR));
    }

    @Test
    void testManageBlogTour_NotFound() throws Exception {
        when(tourService.findById(999L)).thenReturn(null);

        mockMvc.perform(get("/manageBlog/tour/999")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"+ViewPaths.MANAGE_BLOG_TOUR));
    }

    @Test
    void testManageBlogTour_NoBlogs() throws Exception {
        when(tourService.findById(20L)).thenReturn(tour);
        when(blogService.getBlogsByTourId(20L)).thenReturn(List.of());

        mockMvc.perform(get("/manageBlog/tour/20")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"+ViewPaths.MANAGE_BLOG_TOUR));
    }
}