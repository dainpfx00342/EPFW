package funix.epfw.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String gohome() {
        return "home";
    }
    @GetMapping("/")
    public String gohome1() {
        return "home";
    }
}
