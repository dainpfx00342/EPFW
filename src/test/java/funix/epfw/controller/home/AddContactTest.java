package funix.epfw.controller.home;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.Contact;
import funix.epfw.service.home.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddContactTest {

    @Mock
    private ContactService contactService;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    @InjectMocks
    private AddContact addContact;


    @Test
    void testGetContactForm() {
        String view = addContact.contact(model);
        assertEquals(ViewPaths.ADD_CONTACT, view);
        verify(model).addAttribute(eq("contact"), any(Contact.class));
    }

    @Test
    void testSubmitContactForm_Success() {
        Contact contact = new Contact();
        when(result.hasErrors()).thenReturn(false);

        String view = addContact.contactPost(contact, result, model);

        assertEquals(ViewPaths.ADD_CONTACT, view);
        verify(contactService).addContact(contact);
        verify(model).addAttribute(Message.SUCCESS_MESS, "Gửi thông tin thành công");
        verify(model).addAttribute(eq("contact"), any(Contact.class)); // reset contact form
    }

    @Test
    void testSubmitContactForm_WithErrors() {
        Contact contact = new Contact();
        when(result.hasErrors()).thenReturn(true);

        String view = addContact.contactPost(contact, result, model);

        assertEquals(ViewPaths.ADD_CONTACT, view);
        verify(model).addAttribute(Message.ERROR_MESS, "Gửi thông tin không thành công");
        verify(model).addAttribute("contact", contact);
        verify(contactService, never()).addContact(any());
    }
}