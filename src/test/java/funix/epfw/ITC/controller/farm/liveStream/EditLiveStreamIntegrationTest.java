package funix.epfw.ITC.controller.farm.liveStream;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.liveStream.LiveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EditLiveStreamIntegrationTest {

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
    void testGetEditLiveStream_Unauthenticated() throws Exception {
        mockMvc.perform(get("/editLiveStream/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testGetEditLiveStream_LiveStreamNotFound() throws Exception {
        User user = new User();
        user.setRole(Role.FARMER);
        Long invalidId = 999L;
        when(liveService.findById(invalidId)).thenReturn(null);

        mockMvc.perform(get("/editLiveStream/" + invalidId)
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageLiveStream?error=livestreamNotFound"));
    }

    @Test
    void testGetEditLiveStream_Success() throws Exception {
        User user = new User();
        user.setRole(Role.FARMER);
        Long liveId = 1L;
        LiveStream liveStream = new LiveStream();
        liveStream.setId(liveId);

        when(liveService.findById(liveId)).thenReturn(liveStream);

        mockMvc.perform(get("/editLiveStream/" + liveId)
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_LIVE_STREAM))
                .andExpect(model().attributeExists("liveStream"));
    }
    @Test
    void testPostEditLiveStream_Unauthenticated() throws Exception {
        mockMvc.perform(post("/editLiveStream/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Title"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testPostEditLiveStream_LiveStreamNotFound() throws Exception {
        User user = new User();
        user.setRole(Role.FARMER);
        Long id = 100L;
        when(liveService.findById(id)).thenReturn(null);

        mockMvc.perform(post("/editLiveStream/" + id)
                        .sessionAttr("loggedInUser", user)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "Test Title")
                        .param("description", "Test")
                        .param("url", "https://test.com")
                        .param("dateToLive", LocalDateTime.now().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageLiveStream?error=livestreamNotFound"));
    }

}