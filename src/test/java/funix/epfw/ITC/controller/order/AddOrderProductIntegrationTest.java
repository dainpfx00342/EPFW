package funix.epfw.ITC.controller.order;

import funix.epfw.constants.Role;
import funix.epfw.constants.Unit;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.order.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  AddOrderProductIntegrationTest {

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
        public OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private OrderService orderService;

    @Test
    void testGetAddOrderProduct_Unauthenticated() throws Exception {
        mockMvc.perform(get("/addOrder/product/1").param("blogId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testGetAddOrderProduct_ProductNotFound() throws Exception {
        User user = new User();
        user.setRole(Role.BUYER);
        Long productId = 99L;
        Long blogId = 1L;

        when(productService.findById(productId)).thenReturn(null);

        mockMvc.perform(get("/addOrder/product/" + productId)
                        .param("blogId", blogId.toString())
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home?error=productNotFound"));

    }

    @Test
    void testGetAddOrderProduct_Success() throws Exception {
        User user = new User();
        user.setRole(Role.BUYER);
        Long productId = 1L;
        Long blogId = 2L;

        Product product = new Product();
        product.setUnit(Unit.LITRE);
        Blog blog = new Blog();

        when(productService.findById(productId)).thenReturn(product);
        when(blogService.findById(blogId)).thenReturn(blog);

        mockMvc.perform(get("/addOrder/product/" + productId)
                        .param("blogId", blogId.toString())
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_ORDER))
                .andExpect(model().attributeExists("product", "blog", "order"));
    }

    @Test
    void testPostAddOrderProduct_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.BUYER);
        Long productId = 1L;
        Long blogId = 2L;

        Product product = new Product();
        product.setUnit(Unit.KG);
        product.setOrders(new ArrayList<>());

        Blog blog = new Blog();

        when(productService.findById(productId)).thenReturn(product);
        when(blogService.findById(blogId)).thenReturn(blog);

        mockMvc.perform(post("/addOrder/product/" + productId)
                        .param("blogId", blogId.toString())
                        .param("expectedPrice", "30000")
                        .param("orderType", "PRODUCT")
                        .param("orderDate", "2023-10-01")
                        .param("quantity", "10")
                        .param("buyerName", "John Doe")
                        .param("address", "123 Main St")
                        .param("phone", "0123456789")
                        .param("paymentMethod", "CASH")
                        .param("orderStatus", "PENDING")
                        .sessionAttr("loggedInUser", user)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(orderService, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void testPostAddOrderProduct_OrderValidationError() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.BUYER);

        Long productId = 1L;
        Long blogId = 2L;

        Product product = new Product();
        product.setUnit(Unit.KG);
        Blog blog = new Blog();

        when(productService.findById(productId)).thenReturn(product);
        when(blogService.findById(blogId)).thenReturn(blog);

        mockMvc.perform(post("/addOrder/product/" + productId)
                        .param("blogId", blogId.toString())
                        .sessionAttr("loggedInUser", user)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_ORDER))
                .andExpect(model().attributeExists("errorMess"));
    }
}