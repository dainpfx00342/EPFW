package funix.epfw.service.vote;

import funix.epfw.model.vote.Vote;
import funix.epfw.repository.vote.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;

    @Autowired
    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    public void saveVote(Vote vote) {
        voteRepository.save(vote);
    }

    public List<Vote> findByBlogId(Long blogId) {
        return voteRepository.findByBlogId(blogId);
    }
}
