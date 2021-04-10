package com.devactivity.scheduler;

import com.devactivity.repo.RepoService;
import com.devactivity.user.User;
import com.devactivity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RepoScheduler {
    private final UserRepository userRepository;
    private final RepoService repoService;

    /**
     * 6시간마다 repository를 등록합니다.
     */
    @Scheduled(cron = "0 30 */6 * * ?")
    public void repoAddScheduler() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            System.out.println(user.getName());
            repoService.createRepository(user);
        });
    }
}
