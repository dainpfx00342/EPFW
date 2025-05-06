package funix.epfw.ITC.controller.order;

import funix.epfw.constants.Role;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ReviewOrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private OrderService orderService;


    private User currentUser;
    private Order order;

    @BeforeEach
    void setUp() {
        currentUser = new User();
        currentUser.setId(1L);

        order = new Order();
        order.setId(1L);
        order.setUser(currentUser);

        Blog blog = new Blog();
        order.setBlog(blog);



    }

    @Test
    void testReviewOrder_UnAuth() throws Exception{
        mockMvc.perform(get("/reviewOrder/1")
                        .sessionAttr("loggedInUser", new User()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));

    }

    @Test
    void testReviewOrder_UnOwner() throws Exception{
        User otherUser = new User();
        otherUser.setId(2L);
        mockMvc.perform(get("/reviewOrder/1")
                        .sessionAttr("loggedInUser", otherUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/accessDenied"));
    }


    @Test
    void testReviewOrder_GetSuccess() throws Exception{
        currentUser.setRole(Role.BUYER);
        when(orderService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/reviewOrder/1")
                        .sessionAttr("loggedInUser", currentUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.REVIEW_ORDER));
    }

    @Test
    void testReviewOrder_PostSuccess() throws Exception{
        currentUser.setRole(Role.BUYER);
        when(orderService.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/reviewOrder/1")
                        .sessionAttr("loggedInUser", currentUser))
                .andExpect(status().isOk())
                .andExpect(view().name(ViewPaths.REVIEW_ORDER));
    }

}