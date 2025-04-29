package funix.epfw.controller.farm.tour;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.tour.TourService;
import funix.epfw.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManageTourTest {

    @InjectMocks
    private ManageTour manageTour;

    @Mock
    private TourService tourService;

    @Mock
    private UserService userService;

    @Mock
    private FarmService farmService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("farmer01");
    }

    @Test
    void test_manageTour_accessDenied() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = manageTour.manageTour(session, model, null);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_manageTour_withErrorParam() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(userService.findByUsername("farmer01")).thenReturn(mockUser);
            when(farmService.findByUserId(1L)).thenReturn(Collections.emptyList());
            when(tourService.findByFarms(Collections.emptyList())).thenReturn(Collections.emptyList());

            String view = manageTour.manageTour(session, model, "tourNotFound");
            assertEquals(ViewPaths.MANAGE_TOUR, view);
            verify(model).addAttribute(Message.ERROR_MESS, "Tour không tồn tại hoặc đã bị xóa!");
        }
    }

    @Test
    void test_manageTour_success_withTours() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            Tour t1 = new Tour();
            t1.setTourName("Tour 1");
            Tour t2 = new Tour();
            t2.setTourName("Tour 2");

            Farm farm = new Farm();
            List<Farm> farms = List.of(farm);
            List<Tour> tours = List.of(t1, t2);

            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(userService.findByUsername("farmer01")).thenReturn(mockUser);
            when(farmService.findByUserId(1L)).thenReturn(farms);
            when(tourService.findByFarms(farms)).thenReturn(tours);

            String view = manageTour.manageTour(session, model, null);
            assertEquals(ViewPaths.MANAGE_TOUR, view);
            verify(model).addAttribute("tours", tours);
        }
    }


}