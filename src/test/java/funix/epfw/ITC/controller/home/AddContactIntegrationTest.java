package funix.epfw.ITC.controller.home;

import funix.epfw.constants.Message;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.Contact;
import funix.epfw.service.home.ContactService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddContactIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContactService contactService;


    @Test
    void getContactForm_shouldReturnView() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_CONTACT))
                .andExpect(model().attributeExists("contact"));
    }

    @Test
    void postContact_validData_shouldSaveAndShowSuccess() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("name", "Nguyễn Văn A")
                        .param("email", "vana@example.com")
                        .param("message", "Tôi muốn biết thêm thông tin"))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_CONTACT))
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attributeExists(Message.SUCCESS_MESS));

        verify(contactService, times(1)).addContact(any(Contact.class));
    }

    @Test
    void postContact_invalidData_shouldNotSaveAndShowError() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("name", "") // thiếu name
                        .param("email", "abc") // sai định dạng
                        .param("message", "")) // thiếu message
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.ADD_CONTACT))
                .andExpect(model().attributeExists("contact"))
                .andExpect(model().attributeExists(Message.ERROR_MESS));

        verify(contactService, never()).addContact(any());
    }
}