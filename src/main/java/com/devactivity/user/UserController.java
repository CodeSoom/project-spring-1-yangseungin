package com.devactivity.user;

import com.devactivity.user.form.ProfileForm;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 유저와 관련된 HTTP 요청을 처리합니다.
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
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
        model.addAttribute(user);
        if (!Objects.isNull(principal)) {
            model.addAttribute("isOwner", userService.isOwner(principal, user));
        }
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
        model.addAttribute(user);
        model.addAttribute(mapper.map(user, ProfileForm.class));
        return "user/profile-edit";
    }

    /**
     * 유저 프로필 수정 요청을 처리합니다.
     *
     * @param profileForm 수정할 프로필
     * @param principal   로그인한 유저
     * @return 유저 프로필 view
     */
    @PostMapping("/profileedit")
    public String updateProfile(Model model, @Valid ProfileForm profileForm, Errors errors,
                                @AuthenticationPrincipal OAuth2User principal, RedirectAttributes attributes) {
        User user = userService.getUser(principal.getAttribute("login"));
        if (errors.hasErrors()) {
            model.addAttribute(user);
            return "user/profile-edit";
        }
        userService.updateProfile(user, profileForm);
        attributes.addFlashAttribute("message", "프로필을 수정하였습니다.");
        return "redirect:/profile/" + user.getLogin();
    }
}
