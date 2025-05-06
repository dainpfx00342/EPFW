package funix.epfw.ITC.controller.farm.product;


import funix.epfw.constants.Category;
import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.Unit;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeleteProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }
    @Autowired
    private ProductService productService;

    private Product product;
    private User mockUser;

    @BeforeEach
    void setup() {

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");
        mockUser.setRole(Role.FARMER);

        Farm farm = new Farm();
        farm.setId(1L);
        farm.setUser(mockUser);
        // Tạo sản phẩm mẫu
        product = new Product();
        product.setProductName("Test Product");
        product.setPrice(10000);
        product.setNumberOfStock(10);
        product.setDescription("Mô tả test");
        product.setStatus(true);
        product.setUnit(Unit.KG);
        product.setProductCategory(Category.AGRICULTURE);
        product.setFarm(farm);

    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        when(productService.findById(product.getId())).thenReturn(product);

        mockMvc.perform(get("/deleteProduct/1")
                        .sessionAttr("loggedInUser", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageProduct"))
                .andExpect(flash().attribute(Message.SUCCESS_MESS, "Xóa sản phẩm thành công!"));

        // Kiểm tra DB: sản phẩm đã bị xóa
        boolean exists = productService.existsById(product.getId());
        assertFalse(exists);
    }

    @Test
    void testDeleteProduct_Unauthorized() throws Exception {
        // Không có session hợp lệ
        mockMvc.perform(get("/deleteProduct/" + product.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }
}