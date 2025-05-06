package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.liveStream.LiveStream;
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
public class EditLiveStream {
    private final LiveService liveService;

    @Autowired
    public EditLiveStream(LiveService liveService) {
        this.liveService = liveService;
    }

    @GetMapping("/editLiveStream/{liveId}")
    public String editLiveStream(@PathVariable Long liveId, Model model, HttpSession session) {

        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        LiveStream liveStream = liveService.findById(liveId);
        if (liveStream == null) {

            return "redirect:/manageLiveStream?error=livestreamNotFound";
        }
        model.addAttribute("liveStream", liveStream);
        return ViewPaths.EDIT_LIVE_STREAM;
    }

    @PostMapping("/editLiveStream/{liveId}")
    public String updateLiveStream(@PathVariable Long liveId, @Validated @ModelAttribute("liveStream")LiveStream liveStream,
                                   BindingResult result, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        LiveStream liveStreamToUpdate = liveService.findById(liveId);
        if (liveStreamToUpdate == null) {

            return "redirect:/manageLiveStream?error=livestreamNotFound";
        }
        if(result.hasErrors()) {
            model.addAttribute("liveStream", liveStream);
            model.addAttribute(Message.ERROR_MESS,"Sửa lịch không thành công, vui lòng thử lại");
            return ViewPaths.EDIT_LIVE_STREAM;
        }


        liveStreamToUpdate.setTitle(liveStream.getTitle());
        liveStreamToUpdate.setDescription(liveStream.getDescription());
        liveStreamToUpdate.setUrl(liveStream.getUrl());
        liveStreamToUpdate.setDateToLive(liveStream.getDateToLive());


        liveService.saveLive(liveStreamToUpdate);
        return "redirect:/manageLiveStream";
    }
}
