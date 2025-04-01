package funix.epfw.service.farm.liveStream;

import funix.epfw.model.farm.liveStream.LiveStream;
import funix.epfw.repository.farm.liveStream.LiveStreamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LiveService {

    private final LiveStreamRepo liveStreamRepo;

    @Autowired
    public LiveService(LiveStreamRepo liveStreamRepo) {
        this.liveStreamRepo = liveStreamRepo;
    }

    public void saveLive(LiveStream liveStream) {
        liveStreamRepo.save(liveStream);
    }

    public LiveStream findById(Long liveStreamId) {
        return liveStreamRepo.findById(liveStreamId).orElse(null);
    }

    public Object getAllLiveStreams() {
        return liveStreamRepo.findAll();
    }
}
