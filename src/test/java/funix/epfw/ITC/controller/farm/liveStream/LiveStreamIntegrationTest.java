package funix.epfw.ITC.controller.farm.liveStream;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.service.farm.liveStream.LiveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LiveStreamIntegrationTest {
    @TestConfiguration
    static class Config {
     @Bean
        public LiveService liveService() {
            return Mockito.mock(LiveService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LiveService liveService;

    @Test
    void testLiveStreamList_ViewSuccess() throws Exception {
        Farm farm = new Farm();
        farm.setId(1L);
        farm.setFarmName("Trang Trai A");

        LiveStream liveStream = new LiveStream();
        liveStream.setId(1L);
        liveStream.setTitle("Livestream nông sản");
        liveStream.setFarm(farm);

        List<LiveStream> mockList = Collections.singletonList(liveStream);

        when(liveService.getAllLiveStreams()).thenReturn(mockList);

        mockMvc.perform(get("/liveStream"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.LIVE_STREAM))
                .andExpect(model().attributeExists("liveStreams"));
    }
    @Test
    void testLiveStreamList_StatusCodeOk() throws Exception {
        when(liveService.getAllLiveStreams()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/liveStream"))
                .andExpect(status().isOk());
    }

}