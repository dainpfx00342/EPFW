package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManageUserOrderTest {

    @InjectMocks
    private manageUserOrder manageUserOrder;

    @Mock
    private OrderService orderService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private User user;

    @Test
    void testShowManageUserOrder_AuthFailed() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn("redirect:/accessDenied");
            when(session.getAttribute("loggedInUser")).thenReturn(null);

            String result = manageUserOrder.showManageUserOrder(1L, model, session);
            assertEquals("redirect:/accessDenied", result);
        }
    }

    @Test
    void testShowManageUserOrder_OrdersFound() {
            List<Order> orders = List.of(new Order(), new Order());

            try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            when(user.getId()).thenReturn(1L);
            when(orderService.findOrdersByUserId(1L)).thenReturn(orders);
            when(session.getAttribute("loggedInUser")).thenReturn(user);

            String result = manageUserOrder.showManageUserOrder(1L, model, session);
            assertEquals(ViewPaths.MANAGE_ORDER_USER, result);
            verify(model).addAttribute("orders", orders);
            verify(model).addAttribute("user", user);
        }
    }

    @Test
    void testShowManageUserOrder_OrdersNotFound() {
        List<Order> orders = List.of();  // No orders found
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            when(user.getId()).thenReturn(1L);
            when(orderService.findOrdersByUserId(1L)).thenReturn(orders);
            when(session.getAttribute("loggedInUser")).thenReturn(user);

            String result = manageUserOrder.showManageUserOrder(1L, model, session);
            assertEquals(ViewPaths.MANAGE_ORDER_USER, result);
            verify(model).addAttribute("orders", orders);
            verify(model).addAttribute("user", user);
        }
    }
}