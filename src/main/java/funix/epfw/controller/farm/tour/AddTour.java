package funix.epfw.controller.farm.tour;

import funix.epfw.constants.TourType;
import funix.epfw.constants.ViewPaths;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.controller.auth.userAuth.FarmerAuth;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.tour.Tour;
import funix.epfw.service.productService.FarmService;
import funix.epfw.service.productService.TourService;
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
        String authError = authChecker.checkAuth(session);
        if(authError != null) {
            return authError;
        }

        //lấy thông tin trang trại
        System.out.println(farmId);
        Farm currFarm = farmService.findById(farmId);
        if(currFarm == null){
            model.addAttribute("errorMess","Khong tim thay trang trai");
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

        AuthChecker authChecker = new FarmerAuth();
        String authError = authChecker.checkAuth(session);
        if(authError != null) {
            return authError;
        }
        // kiểm tra nếu farm không tồn tại
        Farm currFarm = farmService.findById(farmId);
        if(currFarm == null){
            model.addAttribute("errorMess", "Không tìm thấy trang trại để thêm?");
            return ViewPaths.ADD_TOUR ;
        }

        //Kiểm tra nếu form có lỗi validation
        if(result.hasErrors()){
            model.addAttribute("errorMess", "Thêm mới không thành công");
            model.addAttribute("tourTypes", Arrays.asList(TourType.values()));
            model.addAttribute("currFarm", currFarm);

            return ViewPaths.ADD_TOUR;
        }
        newTour.setFarm(currFarm);
        tourService.addTour(newTour);
        return ViewPaths.MANAGE_TOUR;
    }


}
