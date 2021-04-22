package com.devactivity.scheduler;

import com.devactivity.feed.FeedService;
import com.devactivity.user.User;
import com.devactivity.user.UserRepository;
import com.rometools.rome.io.FeedException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FeedScheduler {
    private final UserRepository userRepository;
    private final FeedService feedService;

    /**
     * 6시간마다 피드를 등록합니다.
     */
    @Scheduled(cron = "0 0 */6 * * ?")
    public void feedAddScheduler() {
        Set<User> AllUserRssNotEmpty = userRepository.findAll()
                .stream().filter(user -> !user.getRssUrl().isBlank())
                .collect(Collectors.toSet());
        AllUserRssNotEmpty.forEach(user -> {
            try {
                feedService.createFeed(user, user.getRssUrl());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FeedException e) {
                e.printStackTrace();
            }
        });
    }
}
