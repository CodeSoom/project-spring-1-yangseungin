package com.devactivity.main;

import com.devactivity.repo.Repo;
import com.devactivity.repo.RepoService;
import com.devactivity.user.User;
import com.devactivity.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final RepoService repoService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (!Objects.isNull(principal)) {
            model.addAttribute("user",principal);
        }
        List<Repo> topTenRepo = repoService.getTopTenRepo();
        model.addAttribute("repos",topTenRepo);

        return "index";
    }

    @GetMapping("/user")
    public String user() {

        return "user";
    }

    @GetMapping("/admin")
    public String admin() {

        return "admin";
    }

    @GetMapping("/search/user")
    public String searchUser(String userName) {
        return "redirect:/profile/" + userName;
    }


}
