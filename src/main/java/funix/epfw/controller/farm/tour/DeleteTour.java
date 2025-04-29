package funix.epfw.controller.farm.tour;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DeleteTour {
    private final TourService tourService;


    @Autowired
    public DeleteTour(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/deleteTour/{tourId}")
    public String deleteTour(@PathVariable Long tourId, HttpSession session, RedirectAttributes model) {

        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        tourService.deleteTourById(tourId);
        model.addFlashAttribute(Message.SUCCESS_MESS, "Xóa tour thành công!");
        return "redirect:/manageTour";
    }
}
