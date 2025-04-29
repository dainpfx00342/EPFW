package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.farm.tour.TourService;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddOrderTourTest {

    @InjectMocks
    private AddOrderTour controller;

    @Mock
    private OrderService orderService;
    @Mock
    private TourService tourService;
    @Mock
    private BlogService blogService;
    @Mock
    private HttpSession session;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;

    private final Long tourId = 1L;
    private final Long blogId = 2L;

    private final Tour mockTour = new Tour();
    private final Blog mockBlog = new Blog();
    private final User mockUser = new User();

    @BeforeEach
    void setUp() {
        mockTour.setTicketPrice(100);
    }
    @Test
    void test_addOrderTour_authFail() {
       try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(ViewPaths.LOGIN);

            String view = controller.addOrderTour(tourId, blogId, model, session);
            assertEquals(ViewPaths.LOGIN, view);
        }
    }

    @Test
    void test_addOrderTour_tourNotFound() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            when(tourService.findById(tourId)).thenReturn(null);

            String view = controller.addOrderTour(tourId, blogId, model, session);

            verify(model).addAttribute(eq(Message.ERROR_MESS), eq("Không tìm thấy tour"));
            assertEquals(ViewPaths.ADD_ORDER_TOUR, view);
        }
    }

    @Test
    void test_addOrderTour_blogNotFound() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            when(tourService.findById(tourId)).thenReturn(mockTour);
            when(blogService.findById(blogId)).thenReturn(null);

            String view = controller.addOrderTour(tourId, blogId, model, session);

            verify(model).addAttribute(eq(Message.ERROR_MESS), eq("Không tìm thấy blog"));
            assertEquals(ViewPaths.ADD_ORDER_TOUR, view);
        }
    }
    // POST METHOD

    @Test
    void test_saveOrderTour_authFail() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(ViewPaths.LOGIN);

            String view = controller.saveOrderTour(tourId, blogId, new Order(), result, model, session);
            assertEquals(ViewPaths.LOGIN, view);
        }
    }

    @Test
    void test_saveOrderTour_tourNotFound() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            when(tourService.findById(tourId)).thenReturn(null);

            String view = controller.saveOrderTour(tourId, blogId, new Order(), result, model, session);

            verify(model).addAttribute(eq(Message.ERROR_MESS), eq("Không tìm thấy tour"));
            assertEquals(ViewPaths.ADD_ORDER_TOUR, view);
        }
    }

    @Test
    void test_saveOrderTour_validationError() {
        Order order = new Order();
        when(tourService.findById(tourId)).thenReturn(mockTour);
        when(blogService.findById(blogId)).thenReturn(mockBlog);
        when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
        when(result.hasErrors()).thenReturn(true);

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            String view = controller.saveOrderTour(tourId, blogId, order, result, model, session);

            verify(model).addAttribute(eq(Message.ERROR_MESS), eq("Vui lòng nhập đầy đủ thông tin"));
            verify(model).addAttribute(eq("tour"), eq(mockTour));
            verify(model).addAttribute(eq("order"), eq(order));
            assertEquals(ViewPaths.ADD_ORDER_TOUR, view);
        }
    }

    @Test
    void test_saveOrderTour_saveOrderException() {
        Order order = new Order();
        when(tourService.findById(tourId)).thenReturn(mockTour);
        when(blogService.findById(blogId)).thenReturn(mockBlog);
        when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
        when(result.hasErrors()).thenReturn(false);
        doThrow(RuntimeException.class).when(orderService).saveOrder(any(Order.class));

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            String view = controller.saveOrderTour(tourId, blogId, order, result, model, session);

            verify(model).addAttribute(eq(Message.ERROR_MESS), eq("Lỗi hệ thống vui lòng thử lại"));
            assertEquals(ViewPaths.ADD_ORDER_TOUR, view);
        }
    }
}