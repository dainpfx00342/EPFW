package funix.epfw.controller.home;

import funix.epfw.constants.AuthUtil;
import funix.epfw.model.Contact;
import funix.epfw.service.home.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@Controller
public class ProcessContact {
    private final ContactService contactService;

    @Autowired
    public ProcessContact(ContactService contactService) {

        this.contactService = contactService;
    }

    @GetMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable Long id, HttpSession session) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }
        contactService.deleteContact(id);
        return "redirect:/manageContact";
    }

    @GetMapping("/doneContact/{id}")
    public String doneContact(@PathVariable Long id, HttpSession session) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }

        Contact contact = contactService.getContactById(id);
        contact.setUpdatedTime(LocalDateTime.now());
        contactService.doneContact(id);
        return "redirect:/manageContact";
    }
}
