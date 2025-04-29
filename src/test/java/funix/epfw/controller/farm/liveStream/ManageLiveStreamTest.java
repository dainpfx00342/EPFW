package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.liveStream.LiveService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ManageLiveStreamTest {

    @InjectMocks
    private ManageLiveStream manageLiveStream;

    @Mock
    private LiveService liveService;

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");
    }

    @Test
    void test_manageLiveStream_accessDenied() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/accessDenied");

            String view = manageLiveStream.manageLiveStream(model, session, null);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_manageLiveStream_withErrorParam() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);

            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(farmService.findByUserId(1L)).thenReturn(Collections.emptyList());
            when(liveService.findByFarms(Collections.emptyList())).thenReturn(Collections.emptyList());

            String view = manageLiveStream.manageLiveStream(model, session, "liveNotFound");
            assertEquals(ViewPaths.MANAGE_LIVE_STREAM, view);
            verify(model).addAttribute(Message.ERROR_MESS, "Live stream không tồn tại hoặc đã bị xóa!");
        }
    }

    @Test
    void test_manageLiveStream_success() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);

            Farm farm = new Farm();
            LiveStream live = new LiveStream();

            List<Farm> farms = List.of(farm);
            List<LiveStream> liveStreams = List.of(live);

            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(farmService.findByUserId(1L)).thenReturn(farms);
            when(liveService.findByFarms(farms)).thenReturn(liveStreams);

            String view = manageLiveStream.manageLiveStream(model, session, null);
            assertEquals(ViewPaths.MANAGE_LIVE_STREAM, view);
            verify(model).addAttribute("liveStreams", liveStreams);
        }
    }
}