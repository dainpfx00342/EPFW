package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageFarmTest {

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private ManageFarm manageFarmController;

    private User farmerUser;
    private User adminUser;
    private List<Farm> farms;

    @BeforeEach
    void setUp() {
        farmerUser = new User();
        farmerUser.setId(1L);
        farmerUser.setUsername("farmer1");
        farmerUser.setRole(Role.FARMER);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setUsername("admin1");
        adminUser.setRole(Role.ADMIN);

        Farm farm = new Farm();
        farm.setId(1L);
        farms = Collections.singletonList(farm);
    }

    @Test
    void testManageFarm_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/unauthorized");

            String view = manageFarmController.manageFarm(model, session, null);

            assertEquals("redirect:/unauthorized", view);
        }
    }

    @Test
    void testManageFarm_WithErrorParam() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(session.getAttribute("loggedInUser")).thenReturn(farmerUser);
            when(farmService.findByUserId(farmerUser.getId())).thenReturn(farms);

            String view = manageFarmController.manageFarm(model, session, "farmNotFound");

            assertEquals(ViewPaths.MANAGE_FRAM, view);
            verify(model).addAttribute(Message.ERROR_MESS, "Trang trại không tồn tại hoặc đã bị xóa!");
            verify(model).addAttribute(eq("user"), eq(farmerUser));
            verify(model).addAttribute(eq("farms"), eq(farms));
        }
    }

    @Test
    void testManageFarm_FarmerUser() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(session.getAttribute("loggedInUser")).thenReturn(farmerUser);
            when(farmService.findByUserId(farmerUser.getId())).thenReturn(farms);

            String view = manageFarmController.manageFarm(model, session, null);

            assertEquals(ViewPaths.MANAGE_FRAM, view);
            verify(model).addAttribute("user", farmerUser);
            verify(model).addAttribute("farms", farms);
        }
    }

    @Test
    void testManageFarm_AdminUser() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(session.getAttribute("loggedInUser")).thenReturn(adminUser);
            when(farmService.findAll()).thenReturn(farms);

            String view = manageFarmController.manageFarm(model, session, null);

            assertEquals(ViewPaths.MANAGE_FRAM, view);
            verify(model).addAttribute("user", adminUser);
            verify(model).addAttribute("farms", farms);
        }
    }
}