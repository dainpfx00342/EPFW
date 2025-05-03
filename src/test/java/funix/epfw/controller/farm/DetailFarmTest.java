package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailFarmTest {

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;
    @Mock
    private Farm mockFarm;

    @InjectMocks
    private DetailFarm detailFarm;


    @BeforeEach
    void setUp() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("testpassword");
        mockUser.setRole(Role.FARMER);

        mockFarm = new Farm();
        mockFarm.setId(1L);
        mockFarm.setFarmName("Test Farm");
        mockFarm.setUser(mockUser);

    }


    @Test
    void testDetailFarmUnauthorized() {
        try(MockedStatic<AuthUtil> authUtilMock = Mockito.mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/accessDenied");
            String view = detailFarm.farmDetail(1L,model, session);

            assertEquals("redirect:/accessDenied", view);
        }
    }
    @Test
    void testDetailFarmNotFound() {
        when(farmService.findById(1L)).thenReturn(null);
        try(MockedStatic<AuthUtil> authUtilMock = Mockito.mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);

            String view = detailFarm.farmDetail(1L,model, session);

            assertEquals("redirect:/manageFarm?error=farmNotFound", view);
            verify(farmService, times(1)).findById(1L);
        }
    }
    @Test
    void testDetailFarmAuthorized() {
        when(farmService.findById(1L)).thenReturn(mockFarm);
        try(MockedStatic<AuthUtil> authUtilMock = Mockito.mockStatic(AuthUtil.class)) {
            authUtilMock.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            String view = detailFarm.farmDetail(1L,model, session);

            assertEquals(ViewPaths.DETAIL_FARM, view);
        }
    }

}