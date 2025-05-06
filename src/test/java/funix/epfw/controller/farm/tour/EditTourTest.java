package funix.epfw.controller.farm.tour;

import funix.epfw.constants.*;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class EditTourTest {

    @InjectMocks
    private EditTour editTour;

    @Mock
    private TourService tourService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    private Tour mockTour;

    @BeforeEach
    void setUp() {
        mockTour = new Tour();
        mockTour.setId(1L);
        mockTour.setTourName("Tour A");
        mockTour.setDescription("Description of Tour A");
        mockTour.setTourType(TourType.FISHING);
        mockTour.setTourStatus(TourStatus.OPENING);
    }

    @Test
    void test_editTour_accessDenied() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = editTour.editTour(1L, model, session);
            assertEquals("redirect:/accessDenied", view);
        }
    }

    @Test
    void test_editTour_formValidationError() {
        when(tourService.findById(1L)).thenReturn(mockTour);
        when(bindingResult.hasErrors()).thenReturn(true);

        String view = editTour.editTour(mockTour, bindingResult, model, 1L, redirectAttributes);
        assertEquals(ViewPaths.EDIT_TOUR, view);
        verify(model).addAttribute(Message.ERROR_MESS, "Vui lòng nhập lại các trường");
    }

    @Test
    void test_editTour_tourNotFound() {
        when(tourService.findById(1L)).thenReturn(null);

        String view = editTour.editTour(mockTour, bindingResult, model, 1L, redirectAttributes);
        assertEquals("redirect:/manageTour?error=tourNotFound", view);

    }

    @Test
    void test_editTour_success() {
        when(tourService.findById(1L)).thenReturn(mockTour);
        when(bindingResult.hasErrors()).thenReturn(false);

        String view = editTour.editTour(mockTour, bindingResult, model, 1L, redirectAttributes);
        assertEquals("redirect:/manageTour", view);
        verify(tourService).saveTour(mockTour);
        verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Cập nhật chuyến du lịch thành công");
    }
}