package floud.demo.service;

import floud.demo.domain.Friendship;
import floud.demo.repository.FriendshipRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final FriendshipRepository friendshipRepository;

    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void autoFriendshipDelete() {
        List<Friendship> friendshipList = friendshipRepository.findAllRejectStatus();
        friendshipRepository.deleteAll(friendshipList);
    }
}