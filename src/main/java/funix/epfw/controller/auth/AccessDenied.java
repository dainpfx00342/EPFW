package funix.epfw.controller.auth;

import funix.epfw.constants.ViewPaths;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDenied {
    @GetMapping("/accessDenied")
    public String accessDenied() {

        return ViewPaths.ACCESS_DENIED;
    }

}
