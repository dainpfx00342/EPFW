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
    private User user;

    @Mock
    private Order order;

    @Mock
    private Blog blog;

    @Test
    void testReviewOrder_AuthFailed() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn("redirect:/login");

            String result = reviewOrder.reviewOrder(1L, model, session);
            assertEquals("redirect:/login", result);
        }
    }

    @Test
    void testReviewOrder_Success() {
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(order);
            when(session.getAttribute("loggedInUser")).thenReturn(user);

            String result = reviewOrder.reviewOrder(1L, model, session);
            assertEquals(ViewPaths.REVIEW_ORDER, result);
            verify(model).addAttribute("order", order);
            verify(model).addAttribute("vote", new Vote());
            verify(model).addAttribute("user", user);
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
        try (MockedStatic<AuthUtil> authUtil = mockStatic(AuthUtil.class)) {
            authUtil.when(() -> AuthUtil.checkBuyerAuth(session)).thenReturn(null);
            when(orderService.findById(1L)).thenReturn(order);
            when(order.getBlog()).thenReturn(blog);
            when(session.getAttribute("loggedInUser")).thenReturn(user);

            Vote vote = new Vote();
            vote.setBlog(blog);
            vote.setOrder(order);

            String result = reviewOrder.reviewOrder(1L, vote, model, session);
            assertEquals("redirect:/manageOrderUser/" + user.getId(), result);
            verify(voteService).saveVote(vote);
            verify(model).addAttribute("order", order);
            verify(model).addAttribute("user", user);
        }
    }
}