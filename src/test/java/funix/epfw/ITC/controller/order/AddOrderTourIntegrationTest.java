package funix.epfw.ITC.controller.order;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.tour.TourService;
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
public class  AddOrderTourIntegrationTest {

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

        @Bean
        public OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TourService tourService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private OrderService orderService;

    @Test
    void testGetAddOrderTour_Unauthenticated() throws Exception {
        mockMvc.perform(get("/addOrder/tour/1").param("blogId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testGetAddOrderTour_TourNotFound() throws Exception {
        User user = new User();
        user.setRole(Role.BUYER);
        Long tourId = 99L;
        Long blogId = 1L;

        when(tourService.findById(tourId)).thenReturn(null);

        mockMvc.perform(get("/addOrder/tour/" + tourId)
                        .param("blogId", blogId.toString())
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home?error=tourNotFound"));
    }

    @Test
    void testGetAddOrderTour_Success() throws Exception {
        User user = new User();
        user.setRole(Role.BUYER);
        Long tourId = 1L;
        Long blogId = 2L;

        Tour tour = new Tour();
        Blog blogTour = new Blog();

        when(tourService.findById(tourId)).thenReturn(tour);
        when(blogService.findById(blogId)).thenReturn(blogTour);

        mockMvc.perform(get("/addOrder/tour/" + tourId)
                        .param("blogId", blogId.toString())
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_ORDER_TOUR))
                .andExpect(model().attributeExists("tour", "blog", "order"));
    }

    @Test
    void testPostAddOrderTour_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.BUYER);
        Long tourId = 1L;
        Long blogId = 2L;

        Tour tour = new Tour();
        tour.setOrders(new ArrayList<>());

        Blog blogTour = new Blog();

        when(tourService.findById(tourId)).thenReturn(tour);
        when(blogService.findById(blogId)).thenReturn(blogTour);

        mockMvc.perform(post("/addOrder/tour/" + tourId)
                        .param("blogId", blogId.toString())
                        .param("expectedPrice", "50000")
                        .param("orderType", "TOUR")
                        .param("orderDate", "2023-10-01")
                        .param("quantity", "5")
                        .param("buyerName", "Jane Smith")
                        .param("address", "456 Street")
                        .param("phone", "0987654321")
                        .param("paymentMethod", "BANK")
                        .param("orderStatus", "PENDING")
                        .sessionAttr("loggedInUser", user)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(orderService, times(1)).saveOrder(any(Order.class));
    }

    @Test
    void testPostAddOrderTour_OrderValidationError() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.BUYER);

        Long tourId = 1L;
        Long blogId = 2L;

        Tour tour = new Tour();
        Blog blogTour = new Blog();

        when(tourService.findById(tourId)).thenReturn(tour);
        when(blogService.findById(blogId)).thenReturn(blogTour);

        mockMvc.perform(post("/addOrder/tour/" + tourId)
                        .param("blogId", blogId.toString())
                        .sessionAttr("loggedInUser", user)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_ORDER_TOUR))
                .andExpect(model().attributeExists("errorMess"));
    }
}