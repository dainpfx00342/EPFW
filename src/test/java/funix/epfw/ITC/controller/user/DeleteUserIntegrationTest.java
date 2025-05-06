package funix.epfw.ITC.controller.user;

import funix.epfw.constants.Role;
import funix.epfw.model.user.User;
import funix.epfw.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class DeleteUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testDeleteUser_GetUnAuth() throws Exception {
        mockMvc.perform(get("/deleteUser/1")
                        .sessionAttr("loggedInUser", new User()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }

    @Test
    void testDeleteUser_GetAuth_Sucess() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setRole(Role.ADMIN);

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/deleteUser/1")
                        .sessionAttr("loggedInUser", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/manageUser"));
            verify(userService).deleteUserById(1L);
    }

}