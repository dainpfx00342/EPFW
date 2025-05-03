package funix.epfw.ITC.controller.farm;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EditFarmIntegrationTest {

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
        mockUser.setRole(Role.FARMER);

        testFarm = new Farm();
        testFarm.setId(1L);
        testFarm.setFarmName("Farm A");
        testFarm.setDescription("A nice farm");
        testFarm.setAddress("123 Farm St");
        testFarm.setCategory(null);
        testFarm.setUser(mockUser);
    }

    // Test: Trường hợp tải trang edit farm thành công
    @Test
    void testEditFarmPageSuccess() throws Exception {
        when(farmService.findById(1L)).thenReturn(testFarm);

        mockMvc.perform(get("/editFarm/1")
                        .sessionAttr("loggedInUser", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_FARM))
                .andExpect(model().attribute("farm", testFarm));

        verify(farmService, times(1)).findById(1L);
    }

    // Test: Trường hợp cập nhật farm thành công
    @Test
    void testEditFarmSuccess() throws Exception {
        Farm updatedFarm = new Farm();
        updatedFarm.setFarmName("Updated Farm");
        updatedFarm.setDescription("Updated description");
        updatedFarm.setAddress("456 New Address");
        updatedFarm.setCategory(null);

        when(farmService.findById(1L)).thenReturn(testFarm);

        mockMvc.perform(post("/editFarm/1")
                        .sessionAttr("loggedInUser", mockUser)
                        .param("farmName", updatedFarm.getFarmName())
                        .param("description", updatedFarm.getDescription())
                        .param("address", updatedFarm.getAddress())
                        .param("category", updatedFarm.getCategory() != null ? updatedFarm.getCategory().name() : ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm"))
                .andExpect(flash().attribute(Message.SUCCESS_MESS, "Cập nhật thông tin farm thành công"));

        verify(farmService, times(1)).findById(1L);
        verify(farmService, times(1)).saveFarm(any(Farm.class));
    }


    @Test
    void testEditFarmValidationFailure() throws Exception {
        Farm invalidFarm = new Farm();
        invalidFarm.setFarmName("");  // Giả sử farmName trống là không hợp lệ
        invalidFarm.setDescription("Invalid description");
        invalidFarm.setAddress("");
        invalidFarm.setCategory(null);

        mockMvc.perform(post("/editFarm/1")
                        .sessionAttr("loggedInUser", mockUser)
                        .param("farmName", invalidFarm.getFarmName())
                        .param("description", invalidFarm.getDescription())
                        .param("address", invalidFarm.getAddress())
                        .param("category", invalidFarm.getCategory() != null ? invalidFarm.getCategory().name() : ""))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_FARM))
                .andExpect(model().attributeHasFieldErrors("farm", "farmName", "address"))
                .andExpect(model().attribute(Message.ERROR_MESS, "Sửa sản phẩm không thành công vui lòng thử lại!"));

        verify(farmService, never()).saveFarm(any(Farm.class));
    }
}