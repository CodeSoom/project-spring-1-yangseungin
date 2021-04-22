package com.devactivity.user;

import com.devactivity.feed.Feed;
import com.devactivity.feed.FeedService;
import com.devactivity.repo.Repo;
import com.devactivity.repo.RepoService;
import com.devactivity.user.form.ProfileForm;
import com.github.dozermapper.core.Mapper;
import com.rometools.rome.io.FeedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * 유저와 관련된 HTTP 요청을 처리합니다.
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FeedService feedService;
    private final RepoService repoService;
    private final UserRepository userRepository;
    private final Mapper mapper;

    /**
     * 이름에 해당하는 유저 프로필을 보여줍니다.
     *
     * @param userName  조회할 username
     * @param principal 로그인한 유저
     * @return 유저 프로필 view
     */
    @GetMapping("/profile/{userName}")
    public String viewProfile(@PathVariable String userName, Model model, @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getUser(userName);
        model.addAttribute("profileUser", user);
        boolean isOwner = false;
        if (!Objects.isNull(principal)) {
            model.addAttribute("user", principal);
            isOwner = userService.isOwner(principal, user);
        }
        model.addAttribute("isOwner", isOwner);
        Set<Feed> feeds = feedService.getFeeds(user);
        model.addAttribute("userFeeds", feeds);

        Set<Repo> repos = repoService.getReposOrderByStar(user);
        model.addAttribute("repos", repos);

        return "user/profile";
    }

    /**
     * 유저 프로필 수정 화면을 보여줍니다.
     *
     * @param principal 로그인한 유저
     * @return 유저 프로필 수정 view
     */
    @GetMapping("/profileedit")
    public String updateProfileForm(Model model, @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getUser(principal.getAttribute("login"));
        model.addAttribute("profileUser", user);
        if (!Objects.isNull(principal)) {
            model.addAttribute("user", principal);
        }
        model.addAttribute(mapper.map(user, ProfileForm.class));
        return "user/profile-edit";
    }

    /**
     * 유저 프로필 수정 요청을 처리합니다.
     * <p>RSS URL을 등록하면 피드를 생성합니다.</p>
     *
     * @param profileForm 수정할 프로필
     * @param principal   로그인한 유저
     * @return 유저 프로필 view
     */
    @PostMapping("/profileedit")
    public String updateProfilde(Model model, @Valid ProfileForm profileForm, Errors errors,
                                 @AuthenticationPrincipal OAuth2User principal, RedirectAttributes attributes) throws IOException, FeedException {
        User user = userService.getUser(principal.getAttribute("login"));
        if (errors.hasErrors()) {
            model.addAttribute(user);
            return "user/profile-edit";
        }

        if (!profileForm.getRssUrl().isBlank()) {
            Set<Feed> feeds = feedService.createFeed(user,profileForm.getRssUrl());
            model.addAttribute("userFeeds", feeds);
        }
        userService.updateProfile(user, profileForm);

        attributes.addFlashAttribute("message", "프로필을 수정하였습니다.");
        return "redirect:/profile/" + user.getLogin();
    }

    /**
     * 유저의 rss url과 피드를 삭제합니다.
     *
     * @param principal 로그인한 유저
     * @return 유저 프로필 view
     */
    @PutMapping("/profileedit")
    public String deleteProfilde(@AuthenticationPrincipal OAuth2User principal, RedirectAttributes attributes) {
        User user = userService.getUser(principal.getAttribute("login"));
        userService.deleteRssUrl(user);

        feedService.deleteFeed(user);

        attributes.addFlashAttribute("message", "rss등록을 해제하였습니다.");
        return "redirect:/profile/" + user.getLogin();
    }


    @GetMapping("/users")
    public String getUsers(@PageableDefault(size = 20, page = 0, sort = "starCount", direction = Sort.Direction.DESC) Pageable pageable, Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (!Objects.isNull(principal)) {
            model.addAttribute("user", principal);
        }
        Page<User> userPage = userRepository.findAll(pageable);
        model.addAttribute("userPage", userPage);

        return "user/all-user";
    }
}
