package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.service.farm.liveStream.LiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailLiveStreamTest {

    @InjectMocks
    private DetailLiveStream detailLiveStream;

    @Mock
    private LiveService liveService;

    @Mock
    private Model model;

    private LiveStream mockLiveStream;

    @BeforeEach
    void setUp() {
        mockLiveStream = new LiveStream();
        mockLiveStream.setId(1L);
        mockLiveStream.setTitle("Test Stream");
    }

    @Test
    void test_detailLiveStream_notFound() {
        when(liveService.findById(1L)).thenReturn(null);

        String view = detailLiveStream.detailLiveStream(1L, model);

        assertEquals("redirect:/manageLiveStream?error=livestreamNotFound", view);
        verify(model).addAttribute(Message.ERROR_MESS, "Không tìm thấy livestream");
    }

    @Test
    void test_detailLiveStream_success() {
        when(liveService.findById(1L)).thenReturn(mockLiveStream);

        String view = detailLiveStream.detailLiveStream(1L, model);

        assertEquals(ViewPaths.DETAIL_LIVE_STREAM, view);
        verify(model).addAttribute("liveStream", mockLiveStream);
    }
}