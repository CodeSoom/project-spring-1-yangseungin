package com.devactivity.feed;

import com.devactivity.errors.UserNotFoundException;
import com.devactivity.user.User;
import com.devactivity.user.UserService;
import com.rometools.rome.io.FeedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class FeedController {
    private final UserService userService;
    private final FeedService feedService;

    @GetMapping("/feed")
    public String getFeedList(Model model, @AuthenticationPrincipal OAuth2User principal) throws IOException, FeedException {
        //TODO 현재 이 메소드는 RSSURL로부터 피드를 생성하고 보여주고있는데 배치로 변경될 예정입니다.
        User user = userService.getUser(principal.getAttribute("login"));
        if (Objects.isNull(user.getRssUrl())) {
            return "error/rss-not-found";
        }

        Set<Feed> feeds = feedService.createFeed(user, user.getRssUrl());
        model.addAttribute("myFeeds", feeds);

        return "feed/feed";
    }

    @DeleteMapping("/feed")
    public String deleteFeed(@AuthenticationPrincipal OAuth2User principal) {
        //TODO 피드삭제는 프로필에서 RSS 등록 해제시 기존에 등록해둔 피드들을 삭제할 예정입니다.
        User user = userService.getUser(principal.getAttribute("login"));
        feedService.deleteFeed(user);

        return "redirect:/profile/" + user.getLogin();
    }

}
