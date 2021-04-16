package com.devactivity.repo;

import com.devactivity.user.User;
import com.devactivity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;


/**page
 * github repository와 관련된 HTTP 요청을 처리합니다.
 */
@Controller
@RequiredArgsConstructor
public class RepoController {

    private final RepoRepository repoRepository;

    @GetMapping("/repos")
    public String getRepos(@PageableDefault(size = 20, page = 0, sort = "starCount", direction = Sort.Direction.DESC) Pageable pageable,
                           Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (!Objects.isNull(principal)) {
            model.addAttribute("user", principal);
        }
        Page<Repo> repoPage = repoRepository.findAll(pageable);
        model.addAttribute("repoPage",repoPage);

        return "repo/all-repo";
    }

}
