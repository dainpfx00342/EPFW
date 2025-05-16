package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Mock
    private RedirectAttributes remodel;


    @Test
    void testDetailOrderUser_AuthFailed() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/accessDenied");

            String result = detailOrderUser.detailOrderUser(1L, model, session,remodel);
            assertEquals("redirect:/accessDenied", result);
        }
    }

    @Test
    void testDetailOrderUser_NoPermission() {
        User mockUser = new User();
        mockUser.setId(1L);

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(orderService.findById(1L)).thenReturn(new Order());
            when(orderService.checkOrderBelongsToUser(1L, 1L)).thenReturn(false);
            when(orderService.checkOrderBelongsOwwer(1L, 1L)).thenReturn(false);

            String result = detailOrderUser.detailOrderUser(1L, model, session, remodel);
            assertEquals("redirect:/manageOrderUser/1", result);
            verify(remodel).addFlashAttribute(Message.ERROR_MESS, "Bạn không có quyền truy cập vào đơn hàng này.");
        }
    }

    @Test
    void testDetailOrderUser_OrderNotFound() {
        User mockUser = new User();
        mockUser.setId(1L);

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);

            when(orderService.findById(1L)).thenReturn(null);

            String result = detailOrderUser.detailOrderUser(1L, model, session, remodel);
            assertEquals("redirect:/manageOrderUser/1", result);
            verify(remodel).addFlashAttribute(Message.ERROR_MESS, "Không tìm thấy đơn hàng.");
        }
    }

    @Test
    void testDetailOrderUser_Success() {
        Order order = new Order();
        User mockUser = new User();
        mockUser.setId(1L);

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            // Mock AuthUtil
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);

            // Mock session để trả về user
            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);

            // Mock các kiểm tra quyền truy cập
            when(orderService.checkOrderBelongsToUser(1L, 1L)).thenReturn(true);


            // Mock trả về đơn hàng hợp lệ
            when(orderService.findById(1L)).thenReturn(order);

            // Gọi hàm
            String result = detailOrderUser.detailOrderUser(1L, model, session, remodel);

            // Kiểm tra kết quả
            assertEquals(ViewPaths.DETAIL_ORDER_USER, result);
            verify(model).addAttribute("order", order);
        }
    }
}