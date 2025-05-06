package funix.epfw.ITC.controller.farm.tour;

import funix.epfw.constants.Role;
import funix.epfw.model.user.User;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeleteTourIntegrationTest {

    @TestConfiguration
    static class Config {
        @Bean
        public TourService tourService() {
            return Mockito.mock(TourService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TourService tourService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("farmer1");
        user.setRole(Role.FARMER);
    }

    @Test
    void testDeleteTour_Success() throws Exception {
        mockMvc.perform(get("/deleteTour/1").sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageTour"));

        verify(tourService, times(1)).deleteTourById(1L);
    }

    @Test
    void testDeleteTour_NotLoggedIn() throws Exception {
        mockMvc.perform(get("/deleteTour/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));

        verify(tourService, never()).deleteTourById(anyLong());
    }


}