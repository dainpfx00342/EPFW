package funix.epfw.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String gohome() {
        return "/home/newHomePage";
    }
    @GetMapping("/")
    public String gohome1() {
        return "/home/newHomePage";
    }
}
