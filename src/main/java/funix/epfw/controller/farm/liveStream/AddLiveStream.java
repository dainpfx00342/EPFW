package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.liveStream.LiveService;
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

@Controller
public class AddLiveStream {
    private final LiveService liveStreamService;
    private final FarmService farmService;

    @Autowired
    public AddLiveStream(LiveService liveStreamService, FarmService farmService) {
        this.liveStreamService = liveStreamService;
        this.farmService = farmService;
    }

    @GetMapping("/addLive/{farmId}")
    public String addLiveStream(@PathVariable Long farmId,
                                Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        Farm farm = farmService.findById(farmId);
        if(farm == null) {
            // Handle error
            return "redirect:/login";
        }
        model.addAttribute("farm", farm);
        model.addAttribute("liveStream", new LiveStream());
        return ViewPaths.ADD_LIVE_STREAM;
    }


    @PostMapping("/addLive/{farmId}")
    public String addLiveStreamPost(@PathVariable Long farmId,
                                    @Validated @ModelAttribute("liveStream") LiveStream liveStream,
                                    BindingResult result,
                                    Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        Farm farm = farmService.findById(farmId);
        if(farm == null) {
            // Handle error
            return "redirect:/login";
        }
        if(result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Thêm livestream không thành công");
            model.addAttribute("farm", farm);
            model.addAttribute("liveStream", liveStream);
            return ViewPaths.ADD_LIVE_STREAM;
        }
        liveStream.setFarm(farm);
        liveStreamService.saveLive(liveStream);
        model.addAttribute("successMessage", "Thêm livestream thành công");
        return ViewPaths.MANAGE_LIVE_STREAM;
    }
}
