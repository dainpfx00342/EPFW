package funix.epfw.ITC.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ConfirmOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private HttpSession session;

    private User currentUser;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1L);

    }

    @Test
    void confirmOrder_userNotAuthorized_shouldRedirectToAccessDenied() throws Exception {
        mockMvc.perform(get("/confirmOrder/1").sessionAttr("loggedInUser", currentUser))
                    .andExpect(redirectedUrl("/accessDenied"));

    }

    @Test
    void confirmOrder_orderNotFound_shouldRedirectToManageOrderProduct() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(null);

            mockMvc.perform(get("/confirmOrder/1").sessionAttr("loggedInUser", currentUser))
                    .andExpect(redirectedUrl("/manageOrderProduct/1"));
        }
    }

    @Test
    void confirmOrder_notOwner_shouldRedirectToAccessDenied() throws Exception {
        Order order = new Order();
        order.setProducts(Collections.emptyList());
        order.setTours(Collections.emptyList());

        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(order);

            mockMvc.perform(get("/confirmOrder/1").sessionAttr("loggedInUser", currentUser))
                    .andExpect(redirectedUrl("/accessDenied"));
        }
    }

    @Test
    void confirmOrder_withProduct_shouldRedirectToManageOrderProduct() throws Exception {
        Farm farm = new Farm();
        farm.setUser(currentUser);
        Product product = new Product();
        product.setFarm(farm);

        Order order = new Order();
        order.setProducts(List.of(product));
        order.setTours(Collections.emptyList());

        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(order);

            mockMvc.perform(get("/confirmOrder/1").sessionAttr("loggedInUser", currentUser))
                    .andExpect(redirectedUrl("/manageOrderProduct/1"));

            verify(orderService).confirmOrder(1L);
        }
    }

    @Test
    void confirmOrder_withTour_shouldRedirectToManageOrderTour() throws Exception {
        Farm farm = new Farm();
        farm.setUser(currentUser);
        Tour tour = new Tour();
        tour.setFarm(farm);

        Order order = new Order();
        order.setProducts(Collections.emptyList());
        order.setTours(List.of(tour));

        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(order);

            mockMvc.perform(get("/confirmOrder/1").sessionAttr("loggedInUser", currentUser))
                    .andExpect(redirectedUrl("/manageOrderTour/1"));

            verify(orderService).confirmOrder(1L);
        }
    }
}