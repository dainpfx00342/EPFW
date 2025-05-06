package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.service.farm.liveStream.LiveService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditLiveStreamTest {

    @InjectMocks
    private EditLiveStream editLiveStream;

    @Mock
    private LiveService liveService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private LiveStream liveStream;

    @BeforeEach
    void setup() {
        liveStream = new LiveStream();
        liveStream.setId(1L);
        liveStream.setTitle("Live Title");
        liveStream.setDescription("Live Desc");
        liveStream.setUrl("http://example.com");
        liveStream.setDateToLive(java.time.LocalDateTime.now());
    }

    // ========== GET Method ==========

    @Test
    void test_editLiveStream_accessDenied() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editLiveStream.editLiveStream(1L, model, session);

            assertEquals("redirect:/manageLiveStream?error=livestreamNotFound", view);
        }
    }

    @Test
    void test_editLiveStream_notFound() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(liveService.findById(1L)).thenReturn(null);

            String view = editLiveStream.editLiveStream(1L, model, session);

            assertEquals("redirect:/manageLiveStream?error=livestreamNotFound", view);

        }
    }

    @Test
    void test_editLiveStream_success() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(liveService.findById(1L)).thenReturn(liveStream);

            String view = editLiveStream.editLiveStream(1L, model, session);

            assertEquals(ViewPaths.EDIT_LIVE_STREAM, view);
            verify(model).addAttribute("liveStream", liveStream);
        }
    }

    // ========== POST Method ==========

    @Test
    void test_updateLiveStream_accessDenied() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editLiveStream.updateLiveStream(1L, liveStream, bindingResult, model, session);

            assertEquals("redirect:/manageLiveStream?error=livestreamNotFound", view);
        }
    }

    @Test
    void test_updateLiveStream_notFound() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(liveService.findById(1L)).thenReturn(null);

            String view = editLiveStream.updateLiveStream(1L, liveStream, bindingResult, model, session);

            assertEquals("redirect:/manageLiveStream?error=livestreamNotFound", view);

        }
    }

    @Test
    void test_updateLiveStream_validationFail() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(liveService.findById(1L)).thenReturn(liveStream);
            when(bindingResult.hasErrors()).thenReturn(true);

            String view = editLiveStream.updateLiveStream(1L, liveStream, bindingResult, model, session);

            assertEquals(ViewPaths.EDIT_LIVE_STREAM, view);
            verify(model).addAttribute("liveStream", liveStream);
            verify(model).addAttribute(Message.ERROR_MESS, "Sửa lịch không thành công, vui lòng thử lại");
        }
    }

    @Test
    void test_updateLiveStream_success() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            when(liveService.findById(1L)).thenReturn(liveStream);
            when(bindingResult.hasErrors()).thenReturn(false);

            String view = editLiveStream.updateLiveStream(1L, liveStream, bindingResult, model, session);

            assertEquals("redirect:/manageLiveStream", view);
            verify(liveService).saveLive(liveStream);
        }
    }
}