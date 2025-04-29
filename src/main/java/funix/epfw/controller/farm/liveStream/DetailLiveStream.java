package funix.epfw.controller.farm.liveStream;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.service.farm.liveStream.LiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailLiveStream {
    private final LiveService liveService;

    @Autowired
    public DetailLiveStream(LiveService liveService) {
        this.liveService = liveService;
    }

    @GetMapping("/detailLiveStream/{liveStreamId}")
    public String detailLiveStream(@PathVariable Long liveStreamId, Model model) {

        LiveStream liveStream = liveService.findById(liveStreamId);
        if (liveStream == null) {
            model.addAttribute(Message.ERROR_MESS,"Không tìm thấy livestream");
            return "redirect:/manageLiveStream?error=livestreamNotFound";
        }
        model.addAttribute("liveStream", liveStream);
        return ViewPaths.DETAIL_LIVE_STREAM;
    }
}
