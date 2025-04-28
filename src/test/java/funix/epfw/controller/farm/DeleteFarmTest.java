package funix.epfw.controller.farm;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.farm.FarmService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteFarmTest {

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private DeleteFarm deleteFarmController;

    @BeforeEach
    void setUp() {
        // Nếu cần chuẩn bị dữ liệu trước mỗi test
    }

    @Test
    void testDeleteFarm_NotAuthorized() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = deleteFarmController.deleteFarm(1L, session, redirectAttributes);

            assertEquals("redirect:/accessDenied", view);
            verifyNoInteractions(farmService);
            verifyNoInteractions(redirectAttributes);
        }
    }

    @Test
    void testDeleteFarm_Success() {
        try (MockedStatic<AuthUtil> authUtilMockedStatic = mockStatic(AuthUtil.class)) {
            authUtilMockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            String view = deleteFarmController.deleteFarm(1L, session, redirectAttributes);

            assertEquals("redirect:/manageFarm", view);
            verify(farmService).deleteFarmById(1L);
            verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Xóa trang trại thành công!");
        }
    }
}