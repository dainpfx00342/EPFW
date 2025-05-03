package funix.epfw.controller.home;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.Contact;
import funix.epfw.service.home.ContactService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DetailContact {
    private final ContactService contactService;

    @Autowired
    public DetailContact(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/detailContact/{id}")
    public String detailContact(@PathVariable Long id, Model model, HttpSession session) {
        String checkAuth = AuthUtil.checkAdminAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }

        Contact contact = contactService.getContactById(id);
        if (contact == null) {
            model.addAttribute(Message.ERROR_MESS, "Không tìm thấy thông tin liên hệ.");
            return "redirect:/manageContact";
        }
        model.addAttribute("contact", contact);
        return ViewPaths.DETAIT_CONTACT;
    }

    @PostMapping("/doneContact/{id}")
    public String doneContact(@PathVariable Long id, HttpSession session) {

        String checkAuth = AuthUtil.checkAdminAuth(session);
        if (checkAuth != null) {
            return checkAuth;
        }

        contactService.doneContact(id);
        return "redirect:/manageContact";
    }

    @DeleteMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/manageContact";
    }
}
