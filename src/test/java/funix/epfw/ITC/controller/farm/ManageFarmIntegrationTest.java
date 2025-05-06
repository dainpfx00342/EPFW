package funix.epfw.ITC.controller.farm;

import funix.epfw.constants.Category;
import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class  ManageFarmIntegrationTest {

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

    private User mockUser;
    private List<Farm> farmList;

    @BeforeEach
    void setUp() {
        reset(farmService);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");
        mockUser.setRole(Role.FARMER);

        Farm farm1 = new Farm();
        farm1.setId(1L);
        farm1.setFarmName("Farm A");
        farm1.setDescription("A nice farm");
        farm1.setAddress("123 Farm St");
        farm1.setCategory(Category.AGRICULTURE);
        farm1.setUser(mockUser);

        Farm farm2 = new Farm();
        farm2.setId(2L);
        farm2.setFarmName("Farm B");
        farm2.setDescription("Another nice farm");
        farm2.setAddress("456 Farm St");
        farm2.setCategory(Category.LIVESTOCK);
        farm2.setUser(mockUser);

        farmList = List.of(farm1, farm2);
    }

    // Test: Trường hợp tải trang manage farm thành công
    @Test
    void testManageFarmPageSuccess() throws Exception {
        when(farmService.findByUserId(mockUser.getId())).thenReturn(farmList);

        mockMvc.perform(get("/manageFarm")
                        .sessionAttr("loggedInUser", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.MANAGE_FRAM))
                .andExpect(model().attribute("farms", farmList))
                .andExpect(model().attribute("user", mockUser));

        verify(farmService, times(1)).findByUserId(mockUser.getId());
    }

    // Test: Trường hợp farm không tồn tại hoặc bị xóa
    @Test
    void testManageFarmPageWithError() throws Exception {
        when(farmService.findByUserId(mockUser.getId())).thenReturn(farmList);

        mockMvc.perform(get("/manageFarm")
                        .sessionAttr("loggedInUser", mockUser)
                        .param("error", "farmNotFound"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.MANAGE_FRAM))
                .andExpect(model().attribute(Message.ERROR_MESS, "Trang trại không tồn tại hoặc đã bị xóa!"))
                .andExpect(model().attribute("farms", farmList))
                .andExpect(model().attribute("user", mockUser));

        verify(farmService, times(1)).findByUserId(mockUser.getId());
    }
}