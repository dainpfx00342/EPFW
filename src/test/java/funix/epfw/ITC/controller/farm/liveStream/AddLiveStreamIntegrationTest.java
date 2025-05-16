package funix.epfw.ITC.controller.farm.liveStream;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.liveStream.LiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  AddLiveStreamIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public FarmService farmService() {
            return Mockito.mock(FarmService.class);
        }

        @Bean
        public LiveService liveService() {
            return Mockito.mock(LiveService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FarmService farmService;


    private Farm farm;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(Role.FARMER);

        farm = new Farm();
        farm.setId(1L);
        farm.setFarmName("Vườn An Phát");
        farm.setUser(user);
    }

    @Test
    void testGetAddLiveStream_validFarmId() throws Exception {
        when(farmService.findById(1L)).thenReturn(farm);

        mockMvc.perform(get("/addLive/1")
                .sessionAttr("loggedInUser", farm.getUser()))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_LIVE_STREAM))
                .andExpect(model().attributeExists("farm"))
                .andExpect(model().attributeExists("liveStream"));
    }

    @Test
    void testGetAddLiveStream_invalidFarmId() throws Exception {
        when(farmService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/addLive/99")
                    .sessionAttr("loggedInUser", farm.getUser()))
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/manageFarm?error=farmNotFound"));

    }

    @Test
    void testPostAddLiveStream_valid() throws Exception {
        when(farmService.findById(1L)).thenReturn(farm);

        mockMvc.perform(post("/addLive/1")
                        .sessionAttr("loggedInUser", farm.getUser())
                        .param("title", "Live mùa thu hoạch")
                        .param("description", "Chia sẻ cách trồng rau hữu cơ"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageLiveStream"));
    }

    @Test
    void testPostAddLiveStream_withErrors() throws Exception {
        when(farmService.findById(1L)).thenReturn(farm);

        mockMvc.perform(post("/addLive/1")
                        .sessionAttr("loggedInUser", farm.getUser())
                        .param("title", "")
                        .param("description", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_LIVE_STREAM))
                .andExpect(model().attributeExists("farm"))
                .andExpect(model().attributeExists("liveStream"))
                .andExpect(model().attributeExists(Message.ERROR_MESS));
    }
}