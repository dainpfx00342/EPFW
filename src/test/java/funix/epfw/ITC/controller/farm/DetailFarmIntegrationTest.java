package funix.epfw.ITC.controller.farm;

import funix.epfw.constants.ViewPaths;
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
public class  DetailFarmIntegrationTest {

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
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");

        testFarm = new Farm();
        testFarm.setId(1L);
        testFarm.setFarmName("Farm A");
        testFarm.setUser(mockUser);



    }

    // Test trường hợp tìm thấy farm và hiển thị chi tiết
    @Test
    void testFarmDetailSuccess() throws Exception {
        when(farmService.findById(1L)).thenReturn(testFarm);

        mockMvc.perform(get("/farmDetail/1")
                        .sessionAttr("loggedInUser", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.DETAIL_FARM))
                .andExpect(model().attribute("farm", testFarm));

        verify(farmService, times(1)).findById(1L);
    }

    // Test trường hợp không tìm thấy farm
    @Test
    void testFarmDetailNotFound() throws Exception {
        when(farmService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/farmDetail/1")
                        .sessionAttr("loggedInUser", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm?error=farmNotFound"));

        verify(farmService, times(1)).findById(1L);
    }

    // Test trường hợp chưa đăng nhập (chưa có quyền truy cập)
    @Test
    void testFarmDetailUnauthorized() throws Exception {
        mockMvc.perform(get("/farmDetail/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));

        verify(farmService, never()).findById(anyLong());
    }
}