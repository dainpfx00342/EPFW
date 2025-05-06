package funix.epfw.ITC.controller.farm.tour;

import funix.epfw.constants.*;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.tour.TourService;
import funix.epfw.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ManageTourIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public TourService tourService() {
            return Mockito.mock(TourService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
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
    private UserService userService;

    @Autowired
    private FarmService farmService;

    private User user;
    private Farm farm;
    private Tour tour;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("farmer1");
        user.setRole(Role.FARMER);

        farm = new Farm();
        farm.setId(1L);
        farm.setUser(user);

        tour = new Tour();
        tour.setId(1L);
        tour.setTourName("Tour A");
        tour.setFarm(farm);
        tour.setTourType(TourType.FISHING);
        tour.setTourStatus(TourStatus.OPENING);
    }

    @Test
    void testManageTour_Success() throws Exception {
        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(farmService.findByUserId(user.getId())).thenReturn(List.of(farm));
        when(tourService.findByFarms(List.of(farm))).thenReturn(List.of(tour));

        mockMvc.perform(get("/manageTour").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.MANAGE_TOUR))
                .andExpect(model().attributeExists("tours"));
    }

    @Test
    void testManageTour_WithErrorParam() throws Exception {
        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(farmService.findByUserId(user.getId())).thenReturn(List.of(farm));
        when(tourService.findByFarms(List.of(farm))).thenReturn(List.of(tour));

        mockMvc.perform(get("/manageTour")
                        .param("error", "tourNotFound")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.MANAGE_TOUR))
                .andExpect(model().attributeExists("tours"))
                .andExpect(model().attributeExists(Message.ERROR_MESS));
    }

    @Test
    void testManageTour_NotLoggedIn() throws Exception {
        mockMvc.perform(get("/manageTour"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }

    @Test
    void testManageTour_NoFarm() throws Exception {
        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(farmService.findByUserId(user.getId())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/manageTour").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.MANAGE_TOUR))
                .andExpect(model().attributeExists("tours"));
    }
}