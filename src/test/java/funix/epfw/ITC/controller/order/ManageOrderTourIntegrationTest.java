package funix.epfw.ITC.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ManageOrderTourIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private User farmer;

    @BeforeEach
    void setUp() {
        farmer = new User();
        farmer.setId(1L);
        farmer.setRole(Role.FARMER);
    }

    @Test
    void manageOrderTour_userNotAuthenticated_shouldRedirect() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(any())).thenReturn("redirect:/accessDenied");

            mockMvc.perform(get("/manageOrderTour/1").sessionAttr("loggedInUser", new User()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/accessDenied"));
        }
    }

    @Test
    void manageOrderProduct_notOwner_shouldRedirectToAccessDenied() throws Exception {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setRole(Role.FARMER);

        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(any())).thenReturn(null);

            mockMvc.perform(get("/manageOrderTour/1")
                            .sessionAttr("loggedInUser", otherUser))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/accessDenied"));
        }
    }

    @Test
    void manageOrderTour_validAccess_shouldReturnView() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkFarmerAuth(any())).thenReturn(null);

            when(orderService.findOrdersProductByUser(1L)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/manageOrderTour/1")
                            .sessionAttr("loggedInUser", farmer))
                    .andExpect(status().isOk())
                    .andExpect(view().name(ViewPaths.MANAGE_ORDER_TOUR))  // Tùy tên ViewPaths.MANAGE_ORDER_PRODUCT
                    .andExpect(model().attributeExists("orders"));
        }
    }

}