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
class DetailOrderTest {

    @InjectMocks
    private DetailOrder detailOrder;

    @Mock
    private OrderService orderService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Test
    void testDetailOrder_AuthFailed() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/login");

            String view = detailOrder.detailOrder(1L, model, session);
            assertEquals("redirect:/login", view);
        }
    }

    @Test
    void testDetailOrder_OrderNotFound() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);

            when(orderService.findById(1L)).thenReturn(null);

            String view = detailOrder.detailOrder(1L, model, session);
            assertEquals("redirect:/order/manageOrderProduct", view);
            verify(model).addAttribute(Message.ERROR_MESS, "Không tìm thấy đơn hàng.");
        }
    }

    @Test
    void testDetailOrder_Success() {
        Order order = new Order();

        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(order);

            String view = detailOrder.detailOrder(1L, model, session);
            assertEquals(ViewPaths.DETAIL_ORDER, view);
            verify(model).addAttribute("order", order);
        }
    }
}