package funix.epfw.ITC.controller.farm.tour;

import funix.epfw.constants.*;
import funix.epfw.model.farm.tour.Tour;
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
public class EditTourIntegrationTest {

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

    private Tour tour;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("farmer1");
        user.setRole(Role.FARMER);

        tour = new Tour();
        tour.setId(1L);
        tour.setTourName("Tour A");
        tour.setTourStatus(TourStatus.OPENING);
        tour.setTourType(TourType.FISHING);
        tour.setDescription("Mô tả");
    }

    @Test
    void testEditTourPage_Success() throws Exception {
        when(tourService.findById(1L)).thenReturn(tour);

        mockMvc.perform(get("/editTour/1").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_TOUR))
                .andExpect(model().attributeExists("tour", "tourTypes", "tourStatuses"));
    }

    @Test
    void testEditTourPage_TourNotFound() throws Exception {
        when(tourService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/editTour/1").sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageTour?error=tourNotFound"));
    }

    @Test
    void testEditTour_SubmitSuccess() throws Exception {
        when(tourService.findById(1L)).thenReturn(tour);

        mockMvc.perform(post("/editTour/1")
                        .sessionAttr("loggedInUser", user)
                        .param("tourName", "Tour A mới")
                        .param("description", "Mô tả mới")
                        .param("tourType", TourType.ECO_TOURISM.name())
                        .param("tourStatus", TourStatus.OPENING.name())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageTour"));

        verify(tourService, times(1)).saveTour(any(Tour.class));
    }

    @Test
    void testEditTour_ValidationError() throws Exception {
        when(tourService.findById(1L)).thenReturn(tour);

        mockMvc.perform(post("/editTour/1")
                        .sessionAttr("loggedInUser", user)
                        .param("tourName", "")  // Gây lỗi
                        .param("description", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.EDIT_TOUR))
                .andExpect(model().attributeExists("tour", "tourTypes", Message.ERROR_MESS));
    }

    @Test
    void testEditTour_TourNotFound() throws Exception {
        when(tourService.findById(1L)).thenReturn(null);

        mockMvc.perform(post("/editTour/1")
                        .sessionAttr("loggedInUser", user)
                        .param("tourName", "Tên tour")
                        .param("description", "Mô tả")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageTour?error=tourNotFound"));
    }
}