package funix.epfw.ITC.controller.farm.product;

import funix.epfw.constants.*;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  EditProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    @Autowired
    private ProductService productService;

    private Product mockProduct;

    @BeforeEach
    void setup() {
        reset(productService);

        User user = new User();
        user.setId(1L);
        user.setUsername("farmer01");
        user.setRole(Role.FARMER);

        Farm farm = new Farm();
        farm.setId(1L);
        farm.setUser(user);

        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setProductName("Old Product");
        mockProduct.setPrice(100);
        mockProduct.setNumberOfStock(20);
        mockProduct.setStatus(true);
        mockProduct.setProductCategory(Category.LIVESTOCK);
        mockProduct.setUnit(Unit.KG);
        mockProduct.setFarm(farm);
    }

    // Test: Truy cập trang chỉnh sửa sản phẩm
    @Test
    void testShowEditProductFormSuccess() throws Exception {
        when(productService.findById(1L)).thenReturn(mockProduct);

        mockMvc.perform(get("/editProduct/1")
                        .sessionAttr("loggedInUser", mockProduct.getFarm().getUser()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_PRODUCT))
                .andExpect(model().attribute("product", mockProduct))
                .andExpect(model().attribute("units", Arrays.asList(Unit.values())))
                .andExpect(model().attribute("categories", Arrays.asList(Category.values())));

        verify(productService, times(1)).findById(1L);
    }

    // Test: Cập nhật sản phẩm thành công
    @Test
    void testEditProductSuccess() throws Exception {
        when(productService.findById(1L)).thenReturn(mockProduct);
        when(productService.saveImage(any(MultipartFile.class))).thenReturn("imageUrl.jpg");

        MockMultipartFile image = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/editProduct/1")
                        .file(image)
                        .param("productName", "Updated Product")
                        .param("numberOfStock", "50")
                        .param("description", "Updated description")
                        .param("status", "true")
                        .param("price", "15000")
                        .param("category", Category.LIVESTOCK.toString())
                        .param("unit", Unit.KG.toString())
                        .param("imageUrl", "image.jpg")
                        .sessionAttr("loggedInUser", mockProduct.getFarm().getUser())
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageProduct"))
                .andExpect(flash().attribute(Message.SUCCESS_MESS, "Cập nhật sản phẩm thành công!"));

        verify(productService, times(1)).saveProduct(any(Product.class));
    }

    // Test: Không tìm thấy sản phẩm để cập nhật
    @Test
    void testEditProductNotFound() throws Exception {
        when(productService.findById(999L)).thenReturn(null);

        MockMultipartFile image = new MockMultipartFile("imageFile", "image.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/editProduct/999")
                        .file(image)
                        .param("productName", "Updated Product")
                        .param("price", "150")
                        .param("numberOfStock", "50")
                        .param("description", "Updated description")
                        .param("status", "true")
                        .param("category", Category.LIVESTOCK.toString())
                        .param("unit", Unit.KG.toString())
                        .sessionAttr("loggedInUser", mockProduct.getFarm().getUser())
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        }))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageProduct"))
                .andExpect(flash().attribute(Message.ERROR_MESS, "Không tìm thấy sản phẩm cần cập nhật!"));

        verify(productService, never()).saveProduct(any());
    }
}