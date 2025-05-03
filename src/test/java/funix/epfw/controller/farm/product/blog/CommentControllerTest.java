package funix.epfw.controller.farm.product.blog;

import funix.epfw.constants.AuthUtil;
import funix.epfw.model.farm.product.Blog;
import funix.epfw.model.user.User;
import funix.epfw.model.vote.Comment;
import funix.epfw.service.farm.product.blog.BlogService;
import funix.epfw.service.vote.CommentService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Mock
    private BlogService blogService;

    @Mock
    private HttpSession session;

    private static final Long BLOG_ID = 1L;
    private static final Long PARENT_ID = 99L;

    private User mockUser;
    private Blog mockBlog;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockBlog = new Blog();
    }

    @Test
    void testAddComment_Success() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkAuth(session)).thenReturn(null);
            Comment comment = new Comment();

            when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
            when(blogService.findById(BLOG_ID)).thenReturn(mockBlog);

            String result = commentController.addComment(BLOG_ID, comment, session);

            assertEquals("redirect:/blogDetail/" + BLOG_ID, result);
            assertEquals(mockUser, comment.getUser());
            assertEquals(mockBlog, comment.getBlog());

            verify(commentService).save(comment);
        }
    }

    @Test
    void testAddComment_CheckAuthFails() {
        try (MockedStatic<AuthUtil> mockedStatic = mockStatic(AuthUtil.class)) {
            mockedStatic.when(() -> AuthUtil.checkAuth(session)).thenReturn("redirect:/login");

            Comment comment = new Comment();

            String result = commentController.addComment(BLOG_ID, comment, session);

            assertEquals("redirect:/login", result);
            verify(commentService, never()).save(any());
        }
    }

    @Test
    void testReplyComment_Success() {
        Comment parent = new Comment();
        parent.setBlog(mockBlog);

        Comment reply = new Comment();

        when(session.getAttribute("loggedInUser")).thenReturn(mockUser);
        when(commentService.findById(PARENT_ID)).thenReturn(parent);

        String result = commentController.replyComment(BLOG_ID, PARENT_ID, reply, session);

        assertEquals("redirect:/blogDetail/" + BLOG_ID, result);
        assertEquals(parent, reply.getParent());
        assertEquals(mockUser, reply.getUser());
        assertEquals(mockBlog, reply.getBlog());
        assertNotNull(reply.getCreatedAt());

        verify(commentService).save(reply);
    }

    @Test
    void testReplyComment_UserNotLoggedIn() {
        when(session.getAttribute("loggedInUser")).thenReturn(null);

        Comment reply = new Comment();
        String result = commentController.replyComment(BLOG_ID, PARENT_ID, reply, session);

        assertEquals("redirect:/login", result);
        verify(commentService, never()).save(any());
    }
}