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

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ManageUserOrderIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private User buyer;

    @BeforeEach
    void setUp() {
        buyer = new User();
        buyer.setId(1L);
        buyer.setRole(Role.BUYER);
    }

    @Test
    void manageOrderUser_userNotAuthenticated_shouldRedirect() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkBuyerAuth(any())).thenReturn("redirect:/accessDenied");

            mockMvc.perform(get("/manageOrderUser/1").sessionAttr("loggedInUser", new User()))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/accessDenied"));
        }
    }

    @Test
    void manageOrderUser_notOwner_shouldRedirectToAccessDenied() throws Exception {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setRole(Role.BUYER);

        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkBuyerAuth(any())).thenReturn(null);

            mockMvc.perform(get("/manageOrderUser/1")
                            .sessionAttr("loggedInUser", otherUser))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/accessDenied"));
        }
    }

    @Test
    void manageOrderUser_validAccess_shouldReturnView() throws Exception {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkBuyerAuth(any())).thenReturn(null);

            when(orderService.findOrdersByUserId(1L)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/manageOrderUser/1")
                            .sessionAttr("loggedInUser", buyer))
                    .andExpect(status().isOk())
                    .andExpect(view().name(ViewPaths.MANAGE_ORDER_USER))  // Sửa theo ViewPaths.MANAGE_ORDER_USER thực tế
                    .andExpect(model().attributeExists("orders"))
                    .andExpect(model().attributeExists("user"));
        }
    }
}