package funix.epfw.ITC.controller.order;

import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CanceledOrderIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }
    }
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderService orderService;

    private User currentUser;
    private MockHttpSession session;

    @BeforeEach
    void setup() {
        Mockito.reset(orderService);
        currentUser = new User();
        currentUser.setId(1L);
        session = new MockHttpSession();
        session.setAttribute("loggedInUser", currentUser);
    }

    @Test
    void cancelOrderProduct_byFarmOwner_shouldSucceed() throws Exception {
        // Setup order with product owned by currentUser
        Order order = new Order();
        Product product = new Product();
        Farm farm = new Farm();

        farm.setUser(currentUser);
        product.setFarm(farm);
        order.setProducts(List.of(product));

        when(orderService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/canceledOrder/product/1")
                        .param("reason", "Test reason")
                        .header("Referer", "/previousPage")
                        .session(session))
                .andExpect(redirectedUrl("/previousPage"));

        verify(orderService).saveOrder(any(Order.class));
    }

    @Test
    void cancelOrderTour_byFarmOwner_shouldSucceed() throws Exception {
        Order order = new Order();
        Tour tour = new Tour();
        Farm farm = new Farm();
        farm.setUser(currentUser);
        tour.setFarm(farm);
        order.setTours(List.of(tour));

        when(orderService.findById(2L)).thenReturn(order);

        mockMvc.perform(get("/canceledOrder/tour/2")
                        .param("reason", "Cancel tour")
                        .header("Referer", "/previousPage")
                        .session(session))
                .andExpect(redirectedUrl("/previousPage"));

        verify(orderService).saveOrder(any(Order.class));
    }

    @Test
    void cancelOrderBuyer_byBuyer_shouldSucceed() throws Exception {
        Order order = new Order();
        order.setUser(currentUser);

        when(orderService.findById(3L)).thenReturn(order);

        mockMvc.perform(get("/canceledOrder/buyer/3")
                        .param("reason", "Buyer cancel")
                        .header("Referer", "/previousPage")
                        .session(session))
                .andExpect(redirectedUrl("/previousPage"));

        verify(orderService).saveOrder(any(Order.class));
    }

    @Test
    void cancelOrderProduct_byWrongUser_shouldFail() throws Exception {
        // Setup farm owned by different user
        User otherUser = new User();
        otherUser.setId(99L);
        User sameUser = currentUser;


        Order order = new Order();
        Product product = new Product();

        Farm farm = new Farm();
        farm.setUser(otherUser);

        product.setFarm(farm);
        order.setProducts(List.of(product));

        when(orderService.findById(4L)).thenReturn(order);

        mockMvc.perform(get("/canceledOrder/product/4")
                        .param("reason", "Invalid cancel")
                        .header("Referer", "/previousPage")
                        .session(session))
                .andExpect(redirectedUrl("/previousPage"));

        verify(orderService, never()).saveOrder(any(Order.class));
    }
}