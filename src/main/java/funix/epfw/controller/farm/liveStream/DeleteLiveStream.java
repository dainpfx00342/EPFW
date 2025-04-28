package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.service.farm.liveStream.LiveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DeleteLiveStream {
    private final LiveService liveService;

    @Autowired
    public DeleteLiveStream(LiveService liveService) {
        this.liveService = liveService;
    }
    @GetMapping("/deleteLiveStream/{liveStreamId}")
    public String deleteLiveStream(@PathVariable Long liveStreamId, HttpSession session) {
        String checkAuth = AuthUtil.checkFarmerAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        liveService.deleteLiveStream(liveStreamId);
        return "redirect:/manageLiveStream";
    }
}
