package funix.epfw.ITC.controller.farm;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  DeleteFarmIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FarmService farmService() {
            return Mockito.mock(FarmService.class);
        }
    }

    @Autowired
    private FarmService farmService;

    private Farm testFarm;
    private User mockUser;

    @BeforeEach
    void setUp() {
        reset(farmService);
        testFarm = new Farm();
        testFarm.setId(1L);
        testFarm.setFarmName("Farm A");

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");
        mockUser.setRole(Role.FARMER);

    }

    @Test
    void testDeleteFarmSuccess() throws Exception {
        when(farmService.findById(1L)).thenReturn(testFarm);
        mockMvc.perform(get("/deleteFarm/1")
                        .sessionAttr("role", Role.FARMER)
                        .sessionAttr("loggedInUser", mockUser)) // Bắt buộc có "user"
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm"))
                .andExpect(flash().attribute(Message.SUCCESS_MESS, "Xóa trang trại thành công!"));

        verify(farmService, times(1)).deleteFarmById(1L);
    }



    @Test
    void testDeleteFarmNotFound() throws Exception {

        when(farmService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/deleteFarm/1")
                        .sessionAttr("loggedInUser", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm"))
                .andExpect(flash().attribute(Message.ERROR_MESS, "Trang trại không tồn tại!"));

        verify(farmService, never()).deleteFarmById(anyLong());
    }

    @Test
    void testDeleteFarmUnauthorized() throws Exception {

        mockUser.setRole(Role.BUYER);
        mockMvc.perform(get("/deleteFarm/1")
                        .sessionAttr("loggedInUser", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));

        verify(farmService, never()).deleteFarmById(anyLong());
    }
}