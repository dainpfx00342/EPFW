package funix.epfw.ITC.controller.farm.tour;

import funix.epfw.constants.Message;
import funix.epfw.constants.Role;
import funix.epfw.constants.TourType;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.tour.TourService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddTourIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public TourService tourService() {
            return Mockito.mock(TourService.class);
        }

        @Bean
        public FarmService farmService() {
            return Mockito.mock(FarmService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TourService tourService;

    @Autowired
    private FarmService farmService;

    private Farm farm;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("farmer1");
        user.setRole(Role.FARMER);

        farm = new Farm();
        farm.setId(1L);
        farm.setUser(user);
        farm.setFarmName("Trang trại 1");
    }

    @Test
    void testToAddTour_Success() throws Exception {
        when(farmService.findById(1L)).thenReturn(farm);

        mockMvc.perform(get("/addTour/1").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_TOUR))
                .andExpect(model().attributeExists("tourTypes", "tourStatuses", "currFarm", "tour"));
    }

    @Test
    void testToAddTour_FarmNotFound() throws Exception {
        when(farmService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/addTour/1").sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm?error=farmNotFound"));
    }

    @Test
    void testAddTour_Success() throws Exception {
        when(farmService.findById(1L)).thenReturn(farm);

        mockMvc.perform(post("/addTour/1")
                        .sessionAttr("loggedInUser", user)
                        .param("tourName", "Tham quan vườn dưa")
                        .param("price", "100000")
                        .param("description", "Rất thú vị")
                        .param("tourType", TourType.FISHING.name())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageTour"));

        verify(tourService, times(1)).saveTour(any(Tour.class));
    }

    @Test
    void testAddTour_ValidationError() throws Exception {
        when(farmService.findById(1L)).thenReturn(farm);

        mockMvc.perform(post("/addTour/1")
                        .sessionAttr("loggedInUser", user)
                        .param("tourName", "") // bỏ trống để gây lỗi validation
                        .param("price", "")
                        .param("description", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_TOUR))
                .andExpect(model().attributeExists(Message.ERROR_MESS, "tourTypes", "currFarm"));
    }

    @Test
    void testAddTour_FarmNotFound() throws Exception {
        when(farmService.findById(1L)).thenReturn(null);

        mockMvc.perform(post("/addTour/1")
                        .sessionAttr("loggedInUser", user)
                        .param("tourName", "Tour ABC")
                        .param("price", "50000")
                        .param("description", "Mô tả tour")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageFarm?error=farmNotFound"));
    }
}