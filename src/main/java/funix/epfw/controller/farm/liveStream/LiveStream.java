package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.ViewPaths;
import funix.epfw.service.farm.liveStream.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LiveStream {
    private final LiveService liveService;

    @Autowired
    public LiveStream(LiveService liveService) {
        this.liveService = liveService;
    }

    @GetMapping("/liveStream")
    public String liveStream(Model model) {
        model.addAttribute("liveStreams", liveService.getAllLiveStreams());
        return ViewPaths.LIVE_STREAM;
    }
}
