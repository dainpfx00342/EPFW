package funix.epfw.ITC.controller.farm;

import funix.epfw.constants.Category;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddFarmIntegrationTest {

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

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer1");
        mockUser.setRole(funix.epfw.constants.Role.FARMER);
    }

    @Test
    void testGetAddFarmWithAuth() throws Exception {
        mockMvc.perform(get("/addFarm")
                        .sessionAttr("loggedInUser", mockUser)
                        .sessionAttr("role", "FARMER"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_FARM))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attributeExists("farm"));
    }

    @Test
    void testPostAddFarmSuccess() throws Exception {
        mockMvc.perform(post("/addFarm")
                        .sessionAttr("loggedInUser", mockUser)
                        .sessionAttr("role", "FARMER")
                        .param("name", "My Farm")
                        .param("location", "Soc Trang")
                        .param("description", "Organic")
                        .param("category", Category.FISHERY.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm"));

        verify(farmService, times(1)).saveFarm(any(Farm.class));
    }

    @Test
    void testPostAddFarmValidationFail() throws Exception {
        mockMvc.perform(post("/addFarm")
                        .sessionAttr("loggedInUser", mockUser)
                        .sessionAttr("role", "FARMER")
                        .param("name", "") // Invalid (empty)
                        .param("location", "")
                        .param("description", "")
                        .param("category", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_FARM))
                .andExpect(model().attributeExists("farm"))
                .andExpect(model().attributeExists("errorMess"));
    }

    @Test
    void testAccessDeniedIfNotFarmer() throws Exception {
        User buyer = new User();
        buyer.setRole(funix.epfw.constants.Role.BUYER);

        mockMvc.perform(get("/addFarm")
                        .sessionAttr("loggedInUser", buyer)
                        .sessionAttr("role", "BUYER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login")); // hoặc tùy checkAuth trả gì

        mockMvc.perform(post("/addFarm")
                        .sessionAttr("loggedInUser", buyer)
                        .sessionAttr("role", "BUYER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}