package funix.epfw.controller.farm.tour;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTourTest {

    @InjectMocks
    private DeleteTour deleteTour;

    @Mock
    private TourService tourService;

    @Mock
    private HttpSession session;

    @Mock
    private RedirectAttributes redirectAttributes;

    private Long tourId;

    @BeforeEach
    void setUp() {
        tourId = 1L; // Gán ID cho tour cần xóa
    }

    @Test
    void test_deleteTour_accessDenied() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn("redirect:/accessDenied");

            String view = deleteTour.deleteTour(tourId, session, redirectAttributes);
            assertEquals("redirect:/accessDenied", view);
            verify(tourService, never()).deleteTourById(anyLong()); // Kiểm tra không gọi service xóa
        }
    }

    @Test
    void test_deleteTour_success() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkFarmerAuth(session)).thenReturn(null);

            String view = deleteTour.deleteTour(tourId, session, redirectAttributes);
            assertEquals("redirect:/manageTour", view);
            verify(tourService).deleteTourById(tourId); // Kiểm tra gọi xóa tour
            verify(redirectAttributes).addFlashAttribute(Message.SUCCESS_MESS, "Xóa tour thành công!"); // Kiểm tra thông báo
        }
    }
}