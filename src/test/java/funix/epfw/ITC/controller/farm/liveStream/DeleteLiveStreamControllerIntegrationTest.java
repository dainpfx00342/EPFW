package funix.epfw.ITC.controller.farm.liveStream;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteLiveStreamControllerIntegrationTest {

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
    void testDeleteLiveStream_withFarmerAuth() throws Exception {
        Long liveStreamId = 1L;

       User user = new User();
        user.setRole(Role.FARMER);

        doNothing().when(liveService).deleteLiveStream(liveStreamId);

        mockMvc.perform(get("/deleteLiveStream/1").sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageLiveStream"))
                .andExpect(flash().attributeExists(Message.SUCCESS_MESS));
    }

    @Test
    void testDeleteLiveStream_withoutAuth() throws Exception {
        User user = new User();
        user.setRole(Role.BUYER);

        mockMvc.perform(get("/deleteLiveStream/2")
                .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }
}