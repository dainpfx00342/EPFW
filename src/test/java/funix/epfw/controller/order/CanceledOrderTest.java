package funix.epfw.controller.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.model.order.Order;
import funix.epfw.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CanceledOrderTest {

    @InjectMocks
    private CanceledOrder controller;

    @Mock
    private OrderService orderService;

    private final String referer = "/manageOrder";

    private Order mockOrder;

    @BeforeEach
    void setUp() {
        mockOrder = new Order();
    }

    @Test
    void test_cancelOrderProduct_success() {
        when(orderService.findById(1L)).thenReturn(mockOrder);

        String result = controller.cancelOrderProduct(1L, "Không còn hàng", referer);

        assertEquals("redirect:/manageOrder", result);
        assertEquals("Đơn hàng bị từ chối bởi người bán. Lý do: Không còn hàng", mockOrder.getNote());
        assertEquals(OrderStatus.CANCELED, mockOrder.getOrderStatus());
        verify(orderService).saveOrder(mockOrder);
    }

    @Test
    void test_cancelOrderTour_success() {
        when(orderService.findById(1L)).thenReturn(mockOrder);

        String result = controller.cancelOrderTour(1L, "Trang trại đóng cửa", referer);

        assertEquals("redirect:/manageOrder", result);
        assertEquals("Đơn đặt tour bị từ chối bởi chủ trang trại. Lý do: Trang trại đóng cửa", mockOrder.getNote());
        assertEquals(OrderStatus.CANCELED, mockOrder.getOrderStatus());
        verify(orderService).saveOrder(mockOrder);
    }

    @Test
    void test_cancelOrderBuyer_success() {
        when(orderService.findById(1L)).thenReturn(mockOrder);

        String result = controller.cancelOrder(1L, "Tôi đổi ý", referer);

        assertEquals("redirect:/manageOrder", result);
        assertEquals("Đơn dặt bị từ chối bởi người mua. Lý do: Tôi đổi ý", mockOrder.getNote());
        assertEquals(OrderStatus.CANCELED, mockOrder.getOrderStatus());
        verify(orderService).saveOrder(mockOrder);
    }

    @Test
    void test_cancelOrder_notFound() {
        when(orderService.findById(1L)).thenReturn(null);

        String result = controller.cancelOrderProduct(1L, "Không còn hàng", referer);

        assertEquals("redirect:/manageOrder", result);
        verify(orderService, never()).saveOrder(any());
    }
}