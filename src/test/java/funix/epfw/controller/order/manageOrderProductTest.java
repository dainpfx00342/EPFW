package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
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
class ManageOrderProductTest {

    @InjectMocks
    private manageOrderProduct manageOrderProduct;

    @Mock
    private OrderService orderService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Test
    void testShowManageOrderUser_AuthFailed() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/login");

            String result = manageOrderProduct.showManageOrderUser(1L, model, session);
            assertEquals("redirect:/login", result);
        }
    }

    @Test
    void testShowManageOrderUser_OrdersFound() {
        List<Order> orders = List.of(new Order(), new Order());  // Mock some orders
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(orderService.findOrdersProductByUser(1L)).thenReturn(orders);

            String result = manageOrderProduct.showManageOrderUser(1L, model, session);
            assertEquals(ViewPaths.MANAGE_ORDER_PRODUCT, result);
            verify(model).addAttribute("orders", orders);
        }
    }

    @Test
    void testShowManageOrderUser_OrdersNotFound() {
        List<Order> orders = List.of();  // No orders found
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(orderService.findOrdersProductByUser(1L)).thenReturn(orders);

            String result = manageOrderProduct.showManageOrderUser(1L, model, session);
            assertEquals(ViewPaths.MANAGE_ORDER_PRODUCT, result);
            verify(model).addAttribute("orders", orders);
        }
    }
}