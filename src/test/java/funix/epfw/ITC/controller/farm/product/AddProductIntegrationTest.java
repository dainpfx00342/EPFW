package funix.epfw.ITC.controller.farm.product;

import funix.epfw.constants.*;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }

        @Bean
        public FarmService farmService() {
            return Mockito.mock(FarmService.class);
        }
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private FarmService farmService;

    private Farm mockFarm;

    @BeforeEach
    void setUp() {
        reset(productService, farmService);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");
        mockUser.setRole(Role.FARMER);

        mockFarm = new Farm();
        mockFarm.setId(1L);
        mockFarm.setFarmName("Farm A");
        mockFarm.setUser(mockUser);

        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setProductName("Product A");
        mockProduct.setPrice(100);
        mockProduct.setFarm(mockFarm);


    }


    @Test
    void testAddProductPageSuccess() throws Exception {
        when(farmService.findById(1L)).thenReturn(mockFarm);

        mockMvc.perform(get("/addProduct/1")
                        .sessionAttr("loggedInUser", mockFarm.getUser()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_PRODUCT))
                .andExpect(model().attribute("farm", mockFarm))
                .andExpect(model().attribute("categories", Arrays.asList(Category.values())))
                .andExpect(model().attribute("units", Arrays.asList(Unit.values())));

        verify(farmService, times(1)).findById(1L);
    }

    // Test: Trường hợp thêm sản phẩm thành công
    @Test
    void testAddProductSuccess() throws Exception {
        when(farmService.findById(1L)).thenReturn(mockFarm);
        when(productService.saveImage(any(MultipartFile.class))).thenReturn("imageUrl");

        MockMultipartFile mockFile = new MockMultipartFile(
                "imageFile",
                "image.jpg",
                "image/jpeg",
                "image content".getBytes()
        );

        mockMvc.perform(multipart("/addProduct/1")
                        .file(mockFile)
                        .sessionAttr("loggedInUser", mockFarm.getUser())
                        .param("productName", "Product A")
                        .param("numberOfStock", "500")
                        .param("description", "Tao huu co")
                        .param("status", "true")
                        .param("price", "1000")
                        .param("category", Category.LIVESTOCK.toString())
                        .param("unit", Unit.KG.toString())
                        .param("imageUrl", "image.jpg"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageProduct"))
                .andExpect(flash().attribute(Message.SUCCESS_MESS, "Sản phẩm được thêm thành công"));

        verify(productService, times(1)).saveOrUpdateProduct(any(Product.class));
    }

    // Test: Trường hợp thêm sản phẩm thất bại (Validation fail)
    @Test
    void testAddProductValidationFailure() throws Exception {
        when(farmService.findById(1L)).thenReturn(mockFarm);
        MockMultipartFile mockFile = new MockMultipartFile("imageFile", "", "image/png", new byte[0]);

        mockMvc.perform(multipart("/addProduct/1")
                        .file(mockFile)
                        .sessionAttr("loggedInUser", mockFarm.getUser())
                        .param("productName", "")
                        .param("price", "1000")
                        .param("category", Category.LIVESTOCK.toString())
                        .param("unit", Unit.KG.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_PRODUCT))
                .andExpect(model().attributeHasFieldErrors("product", "productName"))
                .andExpect(model().attribute(Message.ERROR_MESS, "Vui lòng nhập đầy đủ và chính xác thông tin."));

        verify(productService, never()).saveOrUpdateProduct(any(Product.class));
    }


    @Test
    void testAddProductFarmNotFound() throws Exception {
        when(farmService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/addProduct/1")
                        .sessionAttr("loggedInUser", mockFarm.getUser()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm?error=farmNotFound"))
                .andExpect(flash().attribute(Message.ERROR_MESS, "Không thể tìm thấy trang trại."));

        verify(farmService, times(1)).findById(1L);
    }
}