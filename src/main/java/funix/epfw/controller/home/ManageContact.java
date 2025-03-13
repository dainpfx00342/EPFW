package funix.epfw.controller.home;

import funix.epfw.constants.ViewPaths;
import funix.epfw.controller.auth.userAuth.AdminAuth;
import funix.epfw.controller.auth.userAuth.AuthChecker;
import funix.epfw.model.Contact;
import funix.epfw.service.home.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller

public class ManageContact {
    private final ContactService contactService;

    @Autowired
    public ManageContact(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/manageContact")
    public String manageContact(Model model, HttpSession session) {
        AuthChecker authChecker = new AdminAuth();
        String checkAuth = authChecker.checkAuth(session);
        if(checkAuth != null) {
            return checkAuth;
        }
        List<Contact> contacts = contactService.findAll();
        model.addAttribute("contacts", contacts);
        return ViewPaths.MANAGE_CONTACT;
    }

}
