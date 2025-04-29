package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.ViewPaths;
import funix.epfw.service.farm.liveStream.LiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LiveStreamControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LiveService liveService;

    @InjectMocks
    private LiveStream liveStreamController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(liveStreamController).build();
    }

    @Test
    void testLiveStreamList_ReturnsLiveStreamPage() throws Exception {
        // Giả lập service trả về danh sách rỗng
        when(liveService.getAllLiveStreams()).thenReturn(Collections.emptyList());

        // Gửi yêu cầu GET và kiểm tra kết quả
        mockMvc.perform(get("/liveStream"))
                .andExpect(status().isOk()) // HTTP 200 OK
                .andExpect(view().name(ViewPaths.LIVE_STREAM)) // Kiểm tra view trả về đúng
                .andExpect(model().attributeExists("liveStreams")) // Model có attribute liveStreams
                .andExpect(model().attribute("liveStreams", Collections.emptyList())); // Giá trị đúng
    }
}