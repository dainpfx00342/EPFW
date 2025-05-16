package funix.epfw.controller.order;

import funix.epfw.constants.AuthUtil;
import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.order.Order;
import funix.epfw.model.user.User;
import funix.epfw.model.vote.Vote;
import funix.epfw.service.order.OrderService;
import funix.epfw.service.vote.VoteService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewOrderTest {

    @InjectMocks
    private ReviewOrder reviewOrder;

    @Mock
    private OrderService orderService;

    @Mock
    private VoteService voteService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

       @Mock
    private Order order;

    @Mock
    private Blog blog;

    @Test
    void testReviewOrder_AuthFailed() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn("redirect:/accessDenied");

            String result = reviewOrder.reviewOrder(1L, model, session);
            assertEquals("redirect:/accessDenied", result);
        }
    }

    @Test
    void testReviewOrder_Success() {

        User realUser = new User();
        realUser.setId(5L);

        when(session.getAttribute("loggedInUser")).thenReturn(realUser);
        when(order.getUser()).thenReturn(realUser);
        when(orderService.findById(1L)).thenReturn(order);

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            String view = reviewOrder.reviewOrder(1L, model, session);

            assertEquals(ViewPaths.REVIEW_ORDER, view);
            verify(model).addAttribute("order", order);
            verify(model).addAttribute("vote", new Vote());
            verify(model).addAttribute("user", realUser);
        }
    }

    @Test
    void testSubmitReviewOrder_AuthFailed() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn("redirect:/login");

            String result = reviewOrder.reviewOrder(1L, new Vote(), model, session);
            assertEquals("redirect:/login", result);
        }
    }

    @Test
    void testSubmitReviewOrder_Success() {

        User realUser = new User();
        realUser.setId(7L);

        when(session.getAttribute("loggedInUser")).thenReturn(realUser);
        when(order.getUser()).thenReturn(realUser);
        when(orderService.findById(1L)).thenReturn(order);
        when(order.getBlog()).thenReturn(blog);

        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);

            Vote vote = new Vote();

            String view = reviewOrder.reviewOrder(1L, vote, model, session);

            assertEquals("redirect:/manageOrderUser/" + realUser.getId(), view);
            verify(voteService).saveVote(vote);
            verify(model).addAttribute("order", order);
            verify(model).addAttribute("user", realUser);
        }
    }
}