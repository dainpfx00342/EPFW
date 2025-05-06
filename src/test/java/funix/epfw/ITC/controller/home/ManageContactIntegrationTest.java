package funix.epfw.ITC.controller.home;

import funix.epfw.constants.ContactState;
import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.Contact;
import funix.epfw.model.user.User;
import funix.epfw.service.home.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ManageContactIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;

    @Test
    void getManageContact_withoutAdminSession_shouldRedirect() throws Exception {
        mockMvc.perform(get("/manageContact"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }

    @Test
    void getManageContact_withAdminSession_shouldReturnViewAndData() throws Exception {
       User user = new User();
       user.setRole(Role.ADMIN);

        Contact contact1 = new Contact();
        contact1.setId(1L);
        contact1.setName("Nguyen Van A");
        contact1.setEmail("nguyenvana@gmail.com");
        contact1.setMessage("Hello");
        contact1.setState(ContactState.DONE);

        Contact contact2 = new Contact();
        contact2.setId(2L);
        contact2.setName("Tran Thi B");
        contact2.setEmail("tranthib@gmail.com");
        contact2.setMessage("Support needed");
        contact2.setState(ContactState.NEW);


        List<Contact> fakeContacts = Arrays.asList(contact1, contact2);
        when(contactService.findAll()).thenReturn(fakeContacts);

        mockMvc.perform(get("/manageContact").sessionAttr("loggedInUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.MANAGE_CONTACT))
                .andExpect(model().attributeExists("contacts"));

        verify(contactService, times(1)).findAll();
    }

}