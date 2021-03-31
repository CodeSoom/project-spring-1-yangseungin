package com.devactivity.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Objects;

/**
 * 유저와 관련된 HTTP 요청을 처리합니다.
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 이름에 해당하는 회원 프로필을 보여줍니다.
     *
     * @return 유저 프로필 view
     */
    @GetMapping("/profile/{userName}")
    public String viewProfile(@PathVariable String userName, Model model, @AuthenticationPrincipal OAuth2User principal) {
        User userToView = userService.getUser(userName);
        model.addAttribute(userToView);
        if (!Objects.isNull(principal)) {
            model.addAttribute("isOwner", userService.isOwner(principal, userToView));
        }
        return "user/profile";
    }

}
