package funix.epfw.controller.home;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.Contact;
import funix.epfw.service.home.ContactService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DetailContactTest {

    @InjectMocks
    private DetailContact detailContact;

    @Mock
    private ContactService contactService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Test
    void testDetailContact_AuthFailed() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn("redirect:/login");

            String view = detailContact.detailContact(1L, model, session);
            assertEquals("redirect:/login", view);
        }
    }

    @Test
    void testDetailContact_ContactNotFound() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn(null);

            when(contactService.getContactById(1L)).thenReturn(null);

            String view = detailContact.detailContact(1L, model, session);
            assertEquals("redirect:/manageContact", view);
            verify(model).addAttribute(Message.ERROR_MESS, "Không tìm thấy thông tin liên hệ.");
        }
    }

    @Test
    void testDetailContact_Success() {
        Contact contact = new Contact();
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn(null);

            when(contactService.getContactById(1L)).thenReturn(contact);

            String view = detailContact.detailContact(1L, model, session);
            assertEquals(ViewPaths.DETAIT_CONTACT, view);
            verify(model).addAttribute("contact", contact);
        }
    }

    @Test
    void testDoneContact_AuthFailed() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn("redirect:/login");

            String view = detailContact.doneContact(1L, session);
            assertEquals("redirect:/login", view);
        }
    }

    @Test
    void testDoneContact_Success() {
        try (MockedStatic<AuthUtil> mockedAuth = mockStatic(AuthUtil.class)) {
            mockedAuth.when(() -> AuthUtil.checkAdminAuth(session)).thenReturn(null);

            String view = detailContact.doneContact(1L, session);
            assertEquals("redirect:/manageContact", view);
            verify(contactService).doneContact(1L);
        }
    }

    @Test
    void testDeleteContact() {
        String view = detailContact.deleteContact(1L);
        assertEquals("redirect:/manageContact", view);
        verify(contactService).deleteContact(1L);
    }
}