package funix.epfw.ITC.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CompletedOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private User buyerUser;
    private User otherUser;
    private Order order;

    @BeforeEach
    void setUp() {
        buyerUser = new User();
        buyerUser.setId(1L);
        buyerUser.setRole(Role.BUYER);

        otherUser = new User();
        otherUser.setId(2L);
        otherUser.setRole(Role.BUYER);

        order = new Order();
        order.setId(100L);
        order.setUser(buyerUser);
    }


    @Test
    void completedOrder_userNotAuthenticated_shouldRedirectToAccessDenied() throws Exception {
           mockMvc.perform(get("/completedOrder/100"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/accessDenied"));

    }

    @Test
    void completedOrder_orderNotFound_shouldRedirectToManageOrderProduct() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkBuyerAuth(any())).thenReturn(null);
        }

        when(orderService.findById(100L)).thenReturn(null);

        mockMvc.perform(get("/completedOrder/100")
                        .sessionAttr("loggedInUser", buyerUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageOrderProduct/1"));
    }

    @Test
    void completedOrder_notOrderOwner_shouldRedirectToAccessDenied() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkBuyerAuth(any())).thenReturn(null);
        }

        // Order thuộc user khác
        order.setUser(otherUser);
        when(orderService.findById(100L)).thenReturn(order);

        mockMvc.perform(get("/completedOrder/100")
                        .sessionAttr("loggedInUser", buyerUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }

    @Test
    void completedOrder_validRequest_shouldCompleteOrderAndRedirect() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkBuyerAuth(any())).thenReturn(null);
        }

        when(orderService.findById(100L)).thenReturn(order);

        mockMvc.perform(get("/completedOrder/100")
                        .sessionAttr("loggedInUser", buyerUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageOrderUser/1"))
                .andExpect(flash().attributeExists(Message.SUCCESS_MESS));

        verify(orderService, times(1)).completeOrder(100L);
    }
}