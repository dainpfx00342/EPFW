package funix.epfw.service.vote;

import funix.epfw.model.vote.Comment;
import funix.epfw.repository.vote.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> findByBlogId(Long blogId) {
        return commentRepository.findByBlogId(blogId);
    }
}
