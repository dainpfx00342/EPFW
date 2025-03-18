package funix.epfw.controller.farm.tour;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.TourType;
import funix.epfw.constants.ViewPaths;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FarmerAuth;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.farm.FarmService;
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

import java.util.Arrays;
import java.util.List;

@Controller
public class AddTour {

    private final TourService tourService;
    private final FarmService farmService;

    @Autowired
    public AddTour(TourService tourService, FarmService farmService) {
        this.tourService = tourService;
        this.farmService = farmService;
    }

    @GetMapping("/addTour/{farmId}")
        public String toAddTour(@PathVariable("farmId") Long farmId, Model model, HttpSession session) {

        AuthChecker authChecker = new FarmerAuth();
        String checkAuth = authChecker.checkAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }

        Farm currFarm = farmService.findById(farmId);
        if(currFarm == null){
            model.addAttribute(Message.ERROR_MESS,"Khong tim thay trang trai");
            return ViewPaths.ADD_TOUR;
        }

        List<TourType> tourTypes = Arrays.asList(TourType.values());

        model.addAttribute("tourTypes", tourTypes);

        model.addAttribute("currFarm", currFarm);
        model.addAttribute("tour", new Tour());

        return ViewPaths.ADD_TOUR;
    }

    @PostMapping("/addTour/{farmId}")
    public String addTour (@Validated @ModelAttribute("tour") Tour newTour,
                           BindingResult result,
                           @PathVariable Long farmId,
                           Model model,HttpSession session) {

         String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        // kiểm tra nếu farm không tồn tại
        Farm currFarm = farmService.findById(farmId);
        if(currFarm == null){
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy trang trại để thêm?");
            return ViewPaths.ADD_TOUR ;
        }

        //Kiểm tra nếu form có lỗi validation
        if(result.hasErrors()){
            model.addAttribute(Message.ERROR_MESS, "Thêm mới không thành công");
            model.addAttribute("tourTypes", Arrays.asList(TourType.values()));
            model.addAttribute("currFarm", currFarm);

            return ViewPaths.ADD_TOUR;
        }
        newTour.setFarm(currFarm);
        tourService.saveTour(newTour);
        return "redirect:/manageTour";
    }


}
