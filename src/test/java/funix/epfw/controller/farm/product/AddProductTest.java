package funix.epfw.controller.farm.product;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.product.ProductService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddProductTest {

    @Mock
    private ProductService productService;

    @Mock
    private FarmService farmService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MultipartFile file;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private AddProduct addProductController;

    private Farm farm;

    @BeforeEach
    void setUp() {
        farm = new Farm();
        farm.setId(1L);
    }

    @Test
    void testAddProductPage_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addProductController.addProduct(model, session, 1L,redirectAttributes);

            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void testAddProductPage_FarmNotFound() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(null);

            String view = addProductController.addProduct(model, session, 1L,redirectAttributes);

            assertEquals("redirect:/manageFarm?error=farmNotFound", view);

        }
    }

    @Test
    void testAddProductPage_Success() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(farm);

            String view = addProductController.addProduct(model, session, 1L,redirectAttributes);

            assertEquals(ViewPaths.ADD_PRODUCT, view);
            verify(model).addAttribute(eq("categories"), anyList());
            verify(model).addAttribute(eq("units"), anyList());
            verify(model).addAttribute(eq("product"), any(Product.class));
            verify(model).addAttribute(eq("farm"), eq(farm));
        }
    }

    @Test
    void testAddProductPost_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            Product newProduct = new Product();
            String view = addProductController.addProduct(newProduct, bindingResult, 1L, file, model, session, redirectAttributes);

            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void testAddProductPost_FarmNotFound() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(null);

            Product newProduct = new Product();
            String view = addProductController.addProduct(newProduct, bindingResult, 1L, file, model, session,redirectAttributes);

            assertEquals("redirect:/manageFarm?error=farmNotFound", view);

        }
    }

    @Test
    void testAddProductPost_ValidationErrors() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(farm);
            when(bindingResult.hasErrors()).thenReturn(true);

            Product newProduct = new Product();
            String view = addProductController.addProduct(newProduct, bindingResult, 1L, file, model, session,redirectAttributes);

            assertEquals(ViewPaths.ADD_PRODUCT, view);
            verify(model).addAttribute(eq("categories"), anyList());
            verify(model).addAttribute(eq("units"), anyList());
            verify(model).addAttribute(eq("farm"), eq(farm));
            verify(model).addAttribute(eq(Message.ERROR_MESS), eq("Vui lòng nhập đầy đủ và chính xác thông tin."));
        }
    }

    @Test
    void testAddProductPost_Success() throws IOException {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(farm);
            when(bindingResult.hasErrors()).thenReturn(false);
            when(productService.saveImage(file)).thenReturn("testImageUrl.jpg");

            Product newProduct = new Product();
            String view = addProductController.addProduct(newProduct, bindingResult, 1L, file, model, session,redirectAttributes);

            assertEquals("redirect:/manageProduct", view);
            verify(productService).saveOrUpdateProduct(any(Product.class));
        }
    }
}