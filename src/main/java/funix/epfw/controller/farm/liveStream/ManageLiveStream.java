package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.model.user.User;
import funix.epfw.service.farm.FarmService;
import funix.epfw.service.farm.liveStream.LiveService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManageLiveStream {
    private final LiveService liveService;
    private final FarmService farmService;

    @Autowired
    public ManageLiveStream(LiveService liveService, FarmService farmService) {
        this.liveService = liveService;
        this.farmService = farmService;
    }
    @GetMapping("/manageLiveStream")
    public String manageLiveStream(Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        User currentUser = (User) session.getAttribute("loggedInUser");

        List<Farm> farms = farmService.findByUserId(currentUser.getId());

        List<LiveStream> liveStreams = liveService.findByFarms(farms);

        model.addAttribute("liveStreams", liveStreams);
        return ViewPaths.MANAGE_LIVE_STREAM;
    }
}
