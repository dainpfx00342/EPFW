package funix.epfw.controller.home;

import funix.epfw.model.Contact;
import funix.epfw.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contact", new Contact());
        return "home/contact";
    }

    // post method
    @PostMapping("/contact")
    public String contactPost(@Validated @ModelAttribute("contact") Contact contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMess", "Gửi thông tin không thành công");
            return "home/contact";
        }
        contactService.addContact(contact);
        model.addAttribute("successMess", "Gửi thông tin thành công");
        return "home/contact";
    }

}
