package funix.epfw.controller.farm.tour;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.user.UserService;
import funix.epfw.service.farm.tour.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ManageTour {
    private final TourService tourService;
    private final UserService userService;
    private final FarmService farmService;

    @Autowired
    public ManageTour(TourService tourService, UserService userService, FarmService farmService) {
        this.tourService = tourService;
        this.userService = userService;
        this.farmService = farmService;
    }

    @GetMapping("/manageTour")
    public String manageTour(HttpSession session, Model model,
                             @RequestParam(value = "error", required = false) String error) {

        String checkAuth =  AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        if(error != null) {
            if(error.equals("tourNotFound")){
                model.addAttribute(Message.ERROR_MESS, "Tour không tồn tại hoặc đã bị xóa!");
            }
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        currentUser = userService.findByUsername(currentUser.getUsername());

        List<Tour> tours;
        List<Farm> farms = farmService.findByUserId(currentUser.getId());
        tours = tourService.findByFarms(farms);

        model.addAttribute("tours", tours);
        return ViewPaths.MANAGE_TOUR;
    }
}
