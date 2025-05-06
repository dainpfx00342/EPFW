package funix.epfw.controller.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.constants.Role;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CanceledOrderTest {

    @InjectMocks
    private CanceledOrder controller;

    @Mock
    private OrderService orderService;
    @Mock
    private HttpSession session; // THÊM DÒNG NÀY

    private final String referer = "/manageOrder";

    private Order mockOrder;


    @BeforeEach
    void setUp() {
        mockOrder = new Order();
    }

    @Test
    void test_cancelOrderProduct_success() {
        User user = new User();
        user.setRole(Role.FARMER);

        // Mock một farm cho sản phẩm
        Farm farm = new Farm();
        farm.setUser(user);

        Product product = new Product();
        product.setFarm(farm);

        mockOrder.setProducts(Collections.singletonList(product));  // Thêm sản phẩm vào đơn hàng
        when(orderService.findById(1L)).thenReturn(mockOrder);
        when(session.getAttribute("loggedInUser")).thenReturn(user);

        String result = controller.cancelOrderProduct(1L, "Không còn hàng", referer, session);

        assertEquals("redirect:/manageOrder", result);
        assertEquals("Đơn hàng bị từ chối bởi người bán. Lý do: Không còn hàng", mockOrder.getNote());
        assertEquals(OrderStatus.CANCELED, mockOrder.getOrderStatus());
        verify(orderService).saveOrder(mockOrder);
    }


    @Test
    void test_cancelOrderTour_success() {
        User farmer = new User();
        farmer.setRole(Role.FARMER);
        farmer.setId(1L);

        // Mock một farm cho tour
        Farm farm = new Farm();
        farm.setUser(farmer);

        Tour tour = new Tour();
        tour.setFarm(farm);

        mockOrder.setTours(Collections.singletonList(tour));  // Thêm tour vào đơn hàng
        when(orderService.findById(1L)).thenReturn(mockOrder);
        when(session.getAttribute("loggedInUser")).thenReturn(farmer);

        String result = controller.cancelOrderTour(1L, "Trang trại đóng cửa", referer, session);

        assertEquals("redirect:/manageOrder", result);
        assertEquals("Đơn đặt tour bị từ chối bởi chủ trang trại. Lý do: Trang trại đóng cửa", mockOrder.getNote());
        assertEquals(OrderStatus.CANCELED, mockOrder.getOrderStatus());
        verify(orderService).saveOrder(mockOrder);
    }

    @Test
    void test_cancelOrderBuyer_success() {
        User buyer = new User();
        buyer.setRole(Role.BUYER);
        buyer.setId(1L);
        mockOrder.setUser(buyer);

        when(orderService.findById(1L)).thenReturn(mockOrder);
        when(session.getAttribute("loggedInUser")).thenReturn(buyer);

        String result = controller.cancelOrder(1L, "Tôi đổi ý", referer, session);

        assertEquals("redirect:/manageOrder", result);
        assertEquals("Đơn dặt bị từ chối bởi người mua. Lý do: Tôi đổi ý", mockOrder.getNote());
        assertEquals(OrderStatus.CANCELED, mockOrder.getOrderStatus());
        verify(orderService).saveOrder(mockOrder);
    }

    @Test
    void test_cancelOrder_notFound() {
        // Khi không tìm thấy đơn hàng trong DB
        when(orderService.findById(1L)).thenReturn(null);

        String result = controller.cancelOrderProduct(1L, "Không còn hàng", referer, session);

        assertEquals("redirect:/manageOrder", result);
        verify(orderService, never()).saveOrder(any());
    }

    @Test
    void testCancelOrder_NoPermission_ShouldRedirect() {
        User loggedInUser = new User();
        loggedInUser.setId(2L);
        when(session.getAttribute("loggedInUser")).thenReturn(loggedInUser);

        // Giả lập đơn hàng của người dùng khác
        User orderOwner = new User();
        orderOwner.setId(1L);

        Order order = new Order();
        order.setUser(orderOwner);  // Đơn hàng của người khác
        order.setProducts(Collections.emptyList());  // Sản phẩm trống để không có quyền

        when(orderService.findById(1L)).thenReturn(order);

        String result = controller.cancelOrder(1L, "Lý do test", "product", "http://localhost/test", session);

        assertEquals("redirect:http://localhost/test", result);
    }
}