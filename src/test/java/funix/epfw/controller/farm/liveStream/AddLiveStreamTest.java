package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.service.farm.FarmService;
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
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class AddLiveStreamTest {

    @InjectMocks
    private AddLiveStream addLiveStream;

    @Mock
    private LiveService liveStreamService;

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private Farm farm;

    @BeforeEach
    void setup() {
        farm = new Farm();
        farm.setId(1L);
        farm.setFarmName("Test Farm");
    }

    // ==== GET METHOD TESTS ====

    @Test
    void test_getAddLiveStream_accessDenied() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addLiveStream.addLiveStream(1L, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_getAddLiveStream_farmNotFound() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(null);

            String view = addLiveStream.addLiveStream(1L, model, session);
            assertEquals("redirect:/manageFarm?error=farmNotFound", view);

        }
    }

    @Test
    void test_getAddLiveStream_success() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(farm);

            String view = addLiveStream.addLiveStream(1L, model, session);
            assertEquals(ViewPaths.ADD_LIVE_STREAM, view);
            verify(model).addAttribute("farm", farm);
            verify(model).addAttribute(eq("liveStream"), any(LiveStream.class));
        }
    }

    // ==== POST METHOD TESTS ====

    @Test
    void test_postAddLiveStream_accessDenied() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addLiveStream.addLiveStreamPost(1L, new LiveStream(), bindingResult, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_postAddLiveStream_farmNotFound() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(farmService.findById(1L)).thenReturn(null);

            String view = addLiveStream.addLiveStreamPost(1L, new LiveStream(), bindingResult, model, session);
            assertEquals("redirect:/manageFarm?error=farmNotFound", view);

        }
    }

    @Test
    void test_postAddLiveStream_validationErrors() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            LiveStream liveStream = new LiveStream();

            when(farmService.findById(1L)).thenReturn(farm);
            when(bindingResult.hasErrors()).thenReturn(true);

            String view = addLiveStream.addLiveStreamPost(1L, liveStream, bindingResult, model, session);
            assertEquals(ViewPaths.ADD_LIVE_STREAM, view);
            verify(model).addAttribute(Message.ERROR_MESS, "Thêm livestream không thành công");
            verify(model).addAttribute("farm", farm);
            verify(model).addAttribute("liveStream", liveStream);
        }
    }

    @Test
    void test_postAddLiveStream_success() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            LiveStream liveStream = new LiveStream();

            when(farmService.findById(1L)).thenReturn(farm);
            when(bindingResult.hasErrors()).thenReturn(false);

            String view = addLiveStream.addLiveStreamPost(1L, liveStream, bindingResult, model, session);
            assertEquals("redirect:/manageLiveStream", view);
            verify(liveStreamService).saveLive(liveStream);
        }
    }
}