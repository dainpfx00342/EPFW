package funix.epfw.controller.farm.tour;

import funix.epfw.constants.*;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddTourTest {

    @InjectMocks
    private AddTour addTour;

    @Mock
    private TourService tourService;

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    private final Long farmId = 1L;

    @Test
    void test_toAddTour_accessDenied() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addTour.toAddTour(farmId, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_toAddTour_farmNotFound() {
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(farmId)).thenReturn(null);

            String view = addTour.toAddTour(farmId, model, session);
            assertEquals(ViewPaths.ADD_TOUR, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void test_toAddTour_success() {
        Farm farm = new Farm();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(farmId)).thenReturn(farm);

            String view = addTour.toAddTour(farmId, model, session);
            assertEquals(ViewPaths.ADD_TOUR, view);
            verify(model).addAttribute("tourTypes", Arrays.asList(TourType.values()));
            verify(model).addAttribute("tourStatuses", Arrays.asList(TourStatus.values()));
            verify(model).addAttribute("currFarm", farm);
            verify(model).addAttribute(eq("tour"), any(Tour.class));
        }
    }

    @Test
    void test_addTour_accessDenied() {
        Tour tour = new Tour();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = addTour.addTour(tour, result, farmId, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_addTour_farmNotFound() {
        Tour tour = new Tour();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(farmId)).thenReturn(null);

            String view = addTour.addTour(tour, result, farmId, model, session);
            assertEquals(ViewPaths.ADD_TOUR, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
        }
    }

    @Test
    void test_addTour_validationError() {
        Tour tour = new Tour();
        Farm farm = new Farm();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(farmId)).thenReturn(farm);
            when(result.hasErrors()).thenReturn(true);

            String view = addTour.addTour(tour, result, farmId, model, session);
            assertEquals(ViewPaths.ADD_TOUR, view);
            verify(model).addAttribute(eq(Message.ERROR_MESS), anyString());
            verify(model).addAttribute("currFarm", farm);
            verify(model).addAttribute("tourTypes", Arrays.asList(TourType.values()));
        }
    }

    @Test
    void test_addTour_success() {
        Tour tour = new Tour();
        Farm farm = new Farm();
        try (MockedStatic<AuthUtil> mocked = mockStatic(AuthUtil.class)) {
            mocked.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);
            when(farmService.findById(farmId)).thenReturn(farm);
            when(result.hasErrors()).thenReturn(false);

            String view = addTour.addTour(tour, result, farmId, model, session);
            assertEquals("redirect:/manageTour", view);
            assertEquals(farm, tour.getFarm());
            assertEquals(TourStatus.OPENING, tour.getTourStatus());
            verify(tourService).saveTour(tour);
        }
    }
}