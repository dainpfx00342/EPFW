package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;

import funix.epfw.constants.Message;
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
import org.springframework.validation.BindingResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddFarmTest {

    @Mock
    private FarmService farmService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AddFarm addFarmController;

    private Farm farm;
    private User farmerUser;

    @BeforeEach
    void setUp() {
        farm = new Farm();
        farmerUser = new User();
        farmerUser.setUsername("farmer1");
    }

    @Test
    void testSaveFarm_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addFarmController.saveFarm(model, session);

            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void testSaveFarm_Authorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            String view = addFarmController.saveFarm(model, session);

            assertEquals(ViewPaths.ADD_FARM, view);
            verify(model).addAttribute(eq("categories"), anyList());
            verify(model).addAttribute(eq("farm"), any(Farm.class));
        }
    }

    @Test
    void testAddFarm_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addFarmController.addFarm(farm, bindingResult, model, session);

            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void testAddFarm_FormHasErrors() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(bindingResult.hasErrors()).thenReturn(true);

            String view = addFarmController.addFarm(farm, bindingResult, model, session);

            assertEquals(ViewPaths.ADD_FARM, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void testAddFarm_Success() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(bindingResult.hasErrors()).thenReturn(false);
            when(session.getAttribute("loggedInUser")).thenReturn(farmerUser);

            String view = addFarmController.addFarm(farm, bindingResult, model, session);

            assertEquals("redirect:/manageFarm", view);
            assertEquals(farmerUser, farm.getUser());  // Verify user is set to farm
            verify(farmService).saveFarm(farm);
        }
    }
}