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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditFarmTest {

    @Mock
    private FarmService farmService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private EditFarm editFarmController;

    private Farm farm;

    @BeforeEach
    void setUp() {
        farm = new Farm();
        User farmerUser = new User();
        farmerUser.setUsername("farmer1");
        farm.setId(1L);
        farmerUser.setRole(Role.FARMER);
    }

    @Test
    void testEditFarm_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editFarmController.editFarm(1L, model, session);

            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void testEditFarm_Authorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(1L)).thenReturn(farm);

            String view = editFarmController.editFarm(1L, model, session);

            assertEquals(ViewPaths.EDIT_FARM, view);
            verify(model).addAttribute(eq("categories"), anyList());
            verify(model).addAttribute(eq("farm"), eq(farm));
        }
    }

    @Test
    void testEditFarm_FormHasErrors() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(1L)).thenReturn(farm);
            when(bindingResult.hasErrors()).thenReturn(true);

            String view = editFarmController.editFarm(farm, bindingResult, 1L, model, null);

            assertEquals(ViewPaths.EDIT_FARM, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void testEditFarm_Success() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(1L)).thenReturn(farm);
            when(bindingResult.hasErrors()).thenReturn(false);

            RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

            String view = editFarmController.editFarm(farm, bindingResult, 1L, model, redirectAttributes);

            assertEquals("redirect:/manageFarm", view);
            verify(farmService).saveFarm(farm);
            verify(redirectAttributes).addFlashAttribute(eq(Message.SUCCESS_MESS), anyString());
        }
    }
}