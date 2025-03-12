package funix.epfw.controller.farm.tour;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FarmerAuth;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.model.user.User;
import funix.epfw.service.UserService;
import funix.epfw.service.productService.FarmService;
import funix.epfw.service.productService.TourService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManageTour {
    private final TourService tourService;
    private final UserService userService;

    @Autowired
    public ManageTour(TourService tourService, UserService userService) {
        this.tourService = tourService;
        this.userService = userService;
    }

    @GetMapping("/manageTour")
    public String manageTour(HttpSession session, Model model) {
        AuthChecker authoChecker = new FarmerAuth();
        String accessCheck = authoChecker.checkAuth(session);
        if(accessCheck != null) {
            return accessCheck;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");
        currentUser = userService.findByUsername(currentUser.getUsername());


        List<Tour> tours;
        if(currentUser.getRole() == Role.ADMIN){
            tours = tourService.findAll();

        }else {
            List<Farm> farms = currentUser.getFarms();
            tours = tourService.findByFarms(farms);

        }
        model.addAttribute("tours", tours);
        return ViewPaths.MANAGE_TOUR;
    }
}
