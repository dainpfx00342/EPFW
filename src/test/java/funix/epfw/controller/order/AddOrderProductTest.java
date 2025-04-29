package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.OrderStatus;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddOrderProductTest {

    @InjectMocks
    private AddOrderProduct addOrderProduct;

    @Mock
    private ProductService productService;

    @Mock
    private OrderService orderService;

    @Mock
    private BlogService blogService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    private final Long productId = 1L;
    private final Long blogId = 2L;

    @Test
    void test_addOrder_accessDenied() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addOrderProduct.addOrder(productId, blogId, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_addOrder_productNotFound() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(productService.findById(productId)).thenReturn(null);

            String view = addOrderProduct.addOrder(productId, blogId, model, session);
            assertEquals(ViewPaths.ADD_ORDER, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void test_addOrder_blogNotFound() {
        Product product = new Product();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(productService.findById(productId)).thenReturn(product);
            when(blogService.findById(blogId)).thenReturn(null);

            String view = addOrderProduct.addOrder(productId, blogId, model, session);
            assertEquals(ViewPaths.ADD_ORDER, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void test_addOrder_success() {
        Product product = new Product();
        Blog blog = new Blog();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(productService.findById(productId)).thenReturn(product);
            when(blogService.findById(blogId)).thenReturn(blog);

            String view = addOrderProduct.addOrder(productId, blogId, model, session);
            assertEquals(ViewPaths.ADD_ORDER, view);
            verify(model).addAttribute(eq("product"), eq( product));
            verify(model).addAttribute(eq("order"), any(Order.class));
            verify(model).addAttribute("blog", blog);
        }
    }

    @Test
    void test_processOrder_accessDenied() {
        Order order = new Order();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addOrderProduct.processOrder(productId, blogId, order, result, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_processOrder_userNull() {
        Order order = new Order();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(null);

            String view = addOrderProduct.processOrder(productId, blogId, order, result, model, session);
            assertEquals(ViewPaths.ADD_ORDER, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void test_processOrder_productNull() {
        Order order = new Order();
        User user = new User();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(user);
            when(productService.findById(productId)).thenReturn(null);

            String view = addOrderProduct.processOrder(productId, blogId, order, result, model, session);
            assertEquals(ViewPaths.ADD_ORDER, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void test_processOrder_validationError() {
        Order order = new Order();
        Product product = new Product();
        User user = new User();
        Blog blog = new Blog();

        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(user);
            when(productService.findById(productId)).thenReturn(product);
            when(result.hasErrors()).thenReturn(true);
            when(blogService.findById(blogId)).thenReturn(blog);

            String view = addOrderProduct.processOrder(productId, blogId, order, result, model, session);
            assertEquals(ViewPaths.ADD_ORDER, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
            verify(model).addAttribute("order", order);
            verify(model).addAttribute("product", product);
            verify(model).addAttribute("blog", blog);
        }
    }

    @Test
    void test_processOrder_success() {
        Order order = new Order();
        Product product = new Product();
        product.setPrice(100);
        User user = new User();
        Blog blog = new Blog();

        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(user);
            when(productService.findById(productId)).thenReturn(product);
            when(result.hasErrors()).thenReturn(false);
            when(blogService.findById(blogId)).thenReturn(blog);

            String view = addOrderProduct.processOrder(productId, blogId, order, result, model, session);
            assertEquals("redirect:/home", view);
            assertEquals(user, order.getUser());
            assertEquals(OrderStatus.PENDING, order.getOrderStatus());
            assertEquals("PRODUCT", order.getOrderType());
            verify(orderService).saveOrder(order);
        }
    }

    @Test
    void test_processOrder_exceptionWhenSaving() {
        Order order = new Order();
        Product product = new Product();
        User user = new User();
        Blog blog = new Blog();

        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(session.getAttribute("loggedInUser")).thenReturn(user);
            when(productService.findById(productId)).thenReturn(product);
            when(result.hasErrors()).thenReturn(false);
            when(blogService.findById(blogId)).thenReturn(blog);
            doThrow(RuntimeException.class).when(orderService).saveOrder(order);

            String view = addOrderProduct.processOrder(productId, blogId, order, result, model, session);
            assertEquals(ViewPaths.ADD_ORDER, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }
}