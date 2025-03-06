package funix.epfw.controller.home;

import funix.epfw.model.Contact;
import funix.epfw.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactDetail {
    private final ContactService contactService;

    @Autowired
    public ContactDetail(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/detailContact/{id}")
    public String detailContact(@PathVariable Long id, Model model) {
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "home/detailContact";
    }

    @PostMapping("/doneContact/{id}")
    public String doneContact(@PathVariable Long id) {
        contactService.doneContact(id);
        return "redirect:/manageContact";
    }

    @DeleteMapping("/deleteContact/{id}")
    public String deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return "redirect:/manageContact";
    }
}
