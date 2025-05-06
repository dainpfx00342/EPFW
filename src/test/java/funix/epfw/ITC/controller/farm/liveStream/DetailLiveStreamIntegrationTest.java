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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DetailLiveStreamIntegrationTest {

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
    void testDetailLiveStream_withValidId() throws Exception {
        Farm farm = new Farm();
        farm.setId(1L);
        farm.setFarmName("farm1");
        Long liveStreamId = 1L;
        LiveStream liveStream = new LiveStream();
        liveStream.setId(liveStreamId);
        liveStream.setTitle("Livestream nông sản");
        liveStream.setFarm(farm);



        when(liveService.findById(liveStreamId)).thenReturn(liveStream);

        mockMvc.perform(get("/detailLiveStream/" + liveStreamId))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.DETAIL_LIVE_STREAM))
                .andExpect(model().attributeExists("liveStream"));
    }

    @Test
    void testDetailLiveStream_withInvalidId() throws Exception {
        Long liveStreamId = 999L;

        when(liveService.findById(liveStreamId)).thenReturn(null);

        mockMvc.perform(get("/detailLiveStream/" + liveStreamId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageLiveStream?error=livestreamNotFound"));
    }
}