package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailOrderUserTest {

    @InjectMocks
    private DetailOrderUser detailOrderUser;

    @Mock
    private OrderService orderService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Test
    void testDetailOrderUser_AuthFailed() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/login");

            String result = detailOrderUser.detailOrderUser(1L, model, session);
            assertEquals("redirect:/login", result);
        }
    }

    @Test
    void testDetailOrderUser_OrderNotFound() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(null);

            String result = detailOrderUser.detailOrderUser(1L, model, session);
            assertEquals("redirect:/order/manageOrderUser", result);
            verify(model).addAttribute(Message.ERROR_MESS, "Không tìm thấy đơn hàng.");
        }
    }

    @Test
    void testDetailOrderUser_Success() {
        Order order = new Order();

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(order);

            String result = detailOrderUser.detailOrderUser(1L, model, session);
            assertEquals(ViewPaths.DETAIL_ORDER_USER, result);
            verify(model).addAttribute("order", order);
        }
    }
}