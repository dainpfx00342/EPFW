package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.farm.liveStream.LiveService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteLiveStreamTest {

    @InjectMocks
    private DeleteLiveStream deleteLiveStream;

    @Mock
    private LiveService liveService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Test
    void test_deleteLiveStream_unauthorized() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/login");

            String view = deleteLiveStream.deleteLiveStream(1L, session, redirectAttributes);

            assertEquals("redirect:/login", view);
            verifyNoInteractions(liveService); // không gọi xóa khi chưa login
        }
    }

    @Test
    void test_deleteLiveStream_success() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            String view = deleteLiveStream.deleteLiveStream(1L, session, redirectAttributes);

            assertEquals("redirect:/manageLiveStream", view);
            verify(liveService).deleteLiveStream(1L);
            verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Xóa livestream thành công!");
        }
    }
}