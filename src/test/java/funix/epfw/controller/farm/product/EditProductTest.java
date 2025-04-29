package funix.epfw.controller.farm.product;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Product;
import funix.epfw.service.farm.product.ProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class EditProductTest {

    @InjectMocks
    private EditProduct editProduct;

    @Mock
    private ProductService productService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private RedirectAttributes redirectAttributes;

    private Product mockProduct;

    @BeforeEach
    void setUp() {
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setProductName("Sản phẩm A");
        mockProduct.setPrice(10000);
        mockProduct.setDescription("Mô tả");
        mockProduct.setNumberOfStock(10);
        mockProduct.setStatus(true);
    }

    @Test
    void test_showEditProductForm_accessDenied() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editProduct.showEditProductForm(session, model);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_showEditProductForm_success() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            String view = editProduct.showEditProductForm(session, model);
            assertEquals("redirect:/manageProduct", view);
        }
    }

    @Test
    void test_showEditProductFormById_productNotFound() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(productService.findById(1L)).thenReturn(null);

            String view = editProduct.showEditProductForm(1L, model, session);
            assertEquals("redirect:/manageProduct?error=productNotFound", view);
        }
    }

    @Test
    void test_showEditProductFormById_success() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(productService.findById(1L)).thenReturn(mockProduct);

            String view = editProduct.showEditProductForm(1L, model, session);
            assertEquals(ViewPaths.EDIT_PRODUCT, view);
        }
    }

    @Test
    void test_editProduct_notFound() {
        when(productService.findById(1L)).thenReturn(null);

        String view = editProduct.editProduct(1L, mockProduct, bindingResult, multipartFile, model, redirectAttributes);
        assertEquals("redirect:/manageProduct", view);
        verify(redirectAttributes).addFlashAttribute(eq(Message.ERROR_MESS), anyString());
    }

    @Test
    void test_editProduct_saveImageThrowsException() throws IOException {
        when(productService.findById(1L)).thenReturn(mockProduct);
        doThrow(new IOException("Lỗi")).when(productService).saveImage(multipartFile);
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = editProduct.editProduct(1L, mockProduct, bindingResult, multipartFile, model, redirectAttributes);
        assertEquals("redirect:/manageProduct", view); // vẫn lưu sản phẩm nếu không có lỗi form
    }

    @Test
    void test_editProduct_hasValidationErrors() {
        when(productService.findById(1L)).thenReturn(mockProduct);
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = editProduct.editProduct(1L, mockProduct, bindingResult, multipartFile, model, redirectAttributes);
        assertEquals(ViewPaths.EDIT_PRODUCT, view);
        verify(model).addAttribute(eq(Message.ERROR_MESS), contains("không thành công"));
    }

    @Test
    void test_editProduct_success() throws IOException {
        when(productService.findById(1L)).thenReturn(mockProduct);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.saveImage(multipartFile)).thenReturn("url");

        String view = editProduct.editProduct(1L, mockProduct, bindingResult, multipartFile, model, redirectAttributes);
        assertEquals("redirect:/manageProduct", view);
        verify(productService).saveProduct(any(Product.class));
        verify(redirectAttributes).addFlashAttribute(eq(Message.SUCCESS_MESS), contains("thành công"));
    }
}