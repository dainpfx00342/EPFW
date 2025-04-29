package funix.epfw.controller.farm.product;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ManageProductTest {

    @InjectMocks
    private ManageProduct manageProduct;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");
    }

    @Test
    void test_manageProduct_accessDenied() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = manageProduct.manageProduct(session, model, null);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_manageProduct_withErrorParam() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(userService.findByUsername("farmer01")).thenReturn(mockUser);
            when(farmService.findByUserId(1L)).thenReturn(Collections.emptyList());
            when(productService.findByFarms(Collections.emptyList())).thenReturn(Collections.emptyList());

            String view = manageProduct.manageProduct(session, model, "productNotFound");
            assertEquals(ViewPaths.MANAGE_PRODUCT, view);
            verify(model).addAttribute(Message.ERROR_MESS, "Sản phẩm không tồn tại hoặc đã bị xóa!");
        }
    }

    @Test
    void test_manageProduct_success_withProducts() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Product p1 = new Product();
            p1.setNumberOfStock(0); // sẽ bị setStatus(false)
            Product p2 = new Product();
            p2.setNumberOfStock(5);

            Farm farm = new Farm();
            List<Farm> farms = List.of(farm);
            List<Product> products = List.of(p1, p2);

            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(userService.findByUsername("farmer01")).thenReturn(mockUser);
            when(farmService.findByUserId(1L)).thenReturn(farms);
            when(productService.findByFarms(farms)).thenReturn(products);

            String view = manageProduct.manageProduct(session, model, null);
            assertEquals(ViewPaths.MANAGE_PRODUCT, view);
            verify(model).addAttribute("products", products);
            assertEquals(false, p1.getStatus()); // kiểm tra logic tự động set status
        }
    }
}