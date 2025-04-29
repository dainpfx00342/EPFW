package funix.epfw.controller.farm.product;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;

import funix.epfw.service.farm.product.ProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class DeleteProductTest {

    @InjectMocks
    private DeleteProduct deleteProduct;

    @Mock
    private ProductService productService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    private final Long productId = 1L;

    @Test
    void test_deleteProduct_accessDenied() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = deleteProduct.deleteProduct(productId, redirectAttributes, session);
            assertEquals("redirect:/accessDenied", view);
            verify(productService, never()).deleteProductById(anyLong());
        }
    }

    @Test
    void test_deleteProduct_success() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            String view = deleteProduct.deleteProduct(productId, redirectAttributes, session);
            assertEquals("redirect:/manageProduct", view);
            verify(productService).deleteProductById(productId);
            verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Xóa sản phẩm thành công!");
        }
    }
}