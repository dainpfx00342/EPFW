package funix.epfw.service.vote;

import funix.epfw.model.farm.product.Blog;
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

    public Comment findById(Long parentId) {
        return commentRepository.findById(parentId).orElse(null);
    }

    public List<Comment> findByBlogAndParentNull(Blog blog) {
        return commentRepository.findByBlogAndParentNull(blog);
    }
}
