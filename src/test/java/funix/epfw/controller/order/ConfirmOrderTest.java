package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmOrderTest {

    @InjectMocks
    private ConfirmOrder controller;

    @Mock
    private OrderService orderService;
    @Mock
    private HttpSession session;
    @Mock
    private RedirectAttributes redirectAttributes;

    private User mockUser ;
    private  Order mockOrder;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        mockOrder = new Order();

        // Chuẩn bị product với farm.user = mockUser
        Product product = new Product();
        Farm farm = new Farm();
        farm.setUser(mockUser);
        product.setFarm(farm);
        mockOrder.setProducts(Collections.singletonList(product));

        // Khởi tạo tours rỗng
        mockOrder.setTours(Collections.emptyList());
    }

    @Test
    void test_confirmOrder_authFail() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String result = controller.confirmOrderProduct(1L, redirectAttributes, session);

            assertEquals("redirect:/accessDenied", result);
            verifyNoInteractions(orderService);
        }
    }

    @Test
    void test_confirmOrder_isProductOrder() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(orderService.findById(1L)).thenReturn(mockOrder);

            String result = controller.confirmOrderProduct(1L, redirectAttributes, session);

            assertEquals("redirect:/manageOrderProduct/1", result);
            verify(orderService).confirmOrder(1L);
            verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Xác nhận đơn hàng thành công");
        }
    }

    @Test
    void test_confirmOrder_isTourOrder() {

        mockOrder.setProducts(Collections.emptyList());

        Tour tour = new Tour();
        Farm farm = new Farm();
        farm.setUser(mockUser);
        tour.setFarm(farm);
        mockOrder.setTours(Collections.singletonList(tour));

        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(orderService.findById(1L)).thenReturn(mockOrder);

            String result = controller.confirmOrderProduct(1L, redirectAttributes, session);

            assertEquals("redirect:/manageOrderTour/1", result);
            verify(orderService).confirmOrder(1L);
            verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Xác nhận đơn hàng thành công");
        }
    }

    @Test
    void test_confirmOrder_orderNotFound() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(orderService.findById(1L)).thenReturn(null);

            String result = controller.confirmOrderProduct(1L, redirectAttributes, session);

            assertEquals("redirect:/manageOrderProduct/1", result);
            verify(orderService, never()).confirmOrder(anyLong());
        }
    }
}