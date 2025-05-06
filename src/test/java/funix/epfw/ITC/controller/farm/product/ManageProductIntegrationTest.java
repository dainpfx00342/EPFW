package funix.epfw.ITC.controller.farm.product;

import funix.epfw.constants.*;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.product.Product;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.product.ProductService;
import funix.epfw.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ManageProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private FarmService farmService;


    private User farmer;
    private Farm farm;
    private Product inStock;
    private Product outOfStock;

    @BeforeEach
    void setUp() {
        farmer = new User();
        farmer.setId(1L);
        farmer.setUsername("farmer1");

        farm = new Farm();
        farm.setFarmName("farm1");
        farm.setUser(farmer);

        inStock = new Product();
        inStock.setNumberOfStock(10);
        inStock.setId(1L);
        inStock.setFarm(farm);
        inStock.setProductName("inStock");
        inStock.setStatus(true);
        inStock.setUnit(Unit.LITRE);
        inStock.setProductCategory(Category.LIVESTOCK);

        outOfStock = new Product();
        outOfStock.setProductName("outOfStock");
        outOfStock.setNumberOfStock(0);
        outOfStock.setId(2L);
        outOfStock.setFarm(farm);
        outOfStock.setUnit(Unit.BOTTLE);
        outOfStock.setProductCategory(Category.AGRICULTURE);
        outOfStock.setStatus(false);
    }


    @Test
    void manageProduct_authFail_shouldRedirect() throws Exception {
        mockMvc.perform(get("/manageProduct"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }


    @Test
    void manageProduct_noError_noProducts_shouldShowEmptyList() throws Exception {
        try (MockedStatic<AuthUtil> auth = mockStatic(AuthUtil.class)) {
            auth.when(() -> AuthUtil.checkFarmerAuth(any())).thenReturn(null);
            when(userService.findByUsername("farmer1")).thenReturn(farmer);
            when(farmService.findByUserId(anyLong())).thenReturn(List.of(farm));
            when(productService.findByFarms(List.of(farm))).thenReturn(List.of());

            mockMvc.perform(get("/manageProduct")
                            .sessionAttr("loggedInUser", farmer))
                    .andExpect(status().isOk())
                    .andExpect(view().name(ViewPaths.MANAGE_PRODUCT))
                    .andExpect(model().attribute("products", List.of()));
        }
    }

    // TC3: auth pass, error=productNotFound → hiển thị flash lỗi
    @Test
    void manageProduct_errorProductNotFound_shouldShowError() throws Exception {
        try (MockedStatic<AuthUtil> auth = mockStatic(AuthUtil.class)) {
            auth.when(() -> AuthUtil.checkFarmerAuth(any())).thenReturn(null);
            when(userService.findByUsername("farmer1")).thenReturn(farmer);
            when(farmService.findByUserId(anyLong())).thenReturn(List.of(farm));
            when(productService.findByFarms(List.of(farm))).thenReturn(List.of());

            mockMvc.perform(get("/manageProduct")
                            .sessionAttr("loggedInUser", farmer)
                            .param("error", "productNotFound"))
                    .andExpect(status().isOk())
                    .andExpect(view().name(ViewPaths.MANAGE_PRODUCT))
                    .andExpect(model().attribute(Message.ERROR_MESS, "Sản phẩm không tồn tại hoặc đã bị xóa!"));
        }
    }


    @Test
    void manageProduct_withProducts_shouldAdjustStatusAndShowList() throws Exception {
        try (MockedStatic<AuthUtil> auth = mockStatic(AuthUtil.class)) {
            auth.when(() -> AuthUtil.checkFarmerAuth(any())).thenReturn(null);
            when(userService.findByUsername(anyString())).thenReturn(farmer);
            when(farmService.findByUserId(anyLong())).thenReturn(List.of(farm));
            when(productService.findByFarms(List.of(farm))).thenReturn(List.of(inStock, outOfStock));

            mockMvc.perform(get("/manageProduct")
                            .sessionAttr("loggedInUser", farmer))
                    .andExpect(status().isOk())
                    .andExpect(view().name(ViewPaths.MANAGE_PRODUCT))
                    .andExpect(model().attributeExists("products"));


            assertTrue(inStock.getStatus(), "In-stock product should remain status=true");
            assertFalse(outOfStock.getStatus(), "Out-of-stock product should be set status=false");
        }
    }




}