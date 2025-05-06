package funix.epfw.ITC.controller.home;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.FarmService;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  HomeControllerIntegrationTest {

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

        @Bean
        public FarmService farmService() {
            return Mockito.mock(FarmService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private FarmService farmService;

    private Product product;
    private Blog blog;
    private Farm farm;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setProductName("Dưa hấu");

        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Cách trồng dưa hấu");

        farm = new Farm();
        farm.setId(1L);
        farm.setFarmName("Vườn An Phát");
        farm.setProducts(new ArrayList<>());
        farm.setTours(new ArrayList<>());
    }

    @Test
    void testGoHome() throws Exception {
        when(productService.findAll()).thenReturn(List.of(product));
        when(blogService.getAllBlogs()).thenReturn(List.of(blog));
        when(farmService.findAll()).thenReturn(List.of(farm));

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.HOME))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("blogs"))
                .andExpect(model().attributeExists("farms"));
    }

    @Test
    void testSearchBlog_WithValidKeyword() throws Exception {
        blog.setTitle("Kỹ thuật trồng rau sạch");
        when(blogService.getBlogsByKeyword("rau")).thenReturn(List.of(blog));

        mockMvc.perform(get("/searchBlog").param("keyword", "rau"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.HOME))
                .andExpect(model().attributeExists("blogs"))
                .andExpect(model().attribute("keyword", "rau"));
    }

    @Test
    void testSearchBlog_WithEmptyKeyword() throws Exception {
        when(blogService.getBlogsByKeyword("")).thenReturn(List.of());

        mockMvc.perform(get("/searchBlog").param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.HOME))
                .andExpect(model().attributeExists("blogs"))
                .andExpect(model().attribute("keyword", ""));
    }
}