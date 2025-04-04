package funix.epfw.controller.home;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.Contact;
import funix.epfw.service.home.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AddContact {

    private final ContactService contactService;

    @Autowired
    public AddContact(ContactService contactService) {

        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contact", new Contact());
        return ViewPaths.ADD_CONTACT;
    }

    // post method
    @PostMapping("/contact")
    public String contactPost(@Validated @ModelAttribute("contact") Contact contact,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute(Message.ERROR_MESS, "Gửi thông tin không thành công");
            model.addAttribute("contact", contact);
            return ViewPaths.ADD_CONTACT;
        }
        contactService.addContact(contact);
        model.addAttribute(Message.SUCCESS_MESS, "Gửi thông tin thành công");
        model.addAttribute("contact",new Contact());
        return ViewPaths.ADD_CONTACT;
    }

}
