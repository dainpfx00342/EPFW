package funix.epfw.controller.farm.tour;

import funix.epfw.constants.*;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class EditTour {
    private final TourService tourService;

    @Autowired
    public EditTour(TourService tourService) {

        this.tourService = tourService;
    }

    @GetMapping("/editTour/{tourId}")
    public String editTour(@PathVariable("tourId") Long tourId,
                           Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        List<TourType> tourTypes = Arrays.asList(TourType.values());
        List<TourStatus> tourStatuses = Arrays.asList(TourStatus.values());
        Tour tour = tourService.findById(tourId);
        model.addAttribute("tour", tour);
        model.addAttribute("tourTypes", tourTypes);
        model.addAttribute("tourStatuses", tourStatuses);

        return ViewPaths.EDIT_TOUR;

    }

    @PostMapping("/editTour/{tourId}")
    public String editTour(@Validated @ModelAttribute("tour") Tour currTour,
                           BindingResult result,
                           Model model,
                           @PathVariable Long tourId,
                           RedirectAttributes redirectAttributes) {


        Tour tourToUpdate = tourService.findById(tourId);
        if(tourToUpdate == null) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy tour");
            return "redirect:/manageTour?error=tourNotFound";
        }
        if(result.hasErrors()) {
            model.addAttribute("tour", currTour);
            model.addAttribute(Message.ERROR_MESS, "Vui lòng nhập lại các trường");
            List<TourType> tourTypes = Arrays.asList(TourType.values());
            model.addAttribute("tourTypes", tourTypes);
            return ViewPaths.EDIT_TOUR;
        }
        tourToUpdate.setTourStatus(currTour.getTourStatus());
        tourToUpdate.setTourType(currTour.getTourType());
        tourToUpdate.setTourName(currTour.getTourName());
        tourToUpdate.setDescription(currTour.getDescription());

        tourService.saveTour(tourToUpdate);
        redirectAttributes.addFlashAttribute(Message.SUCCESS_MESS,"Cập nhật chuyến du lịch thành công");
        return "redirect:/manageTour";
    }
}
