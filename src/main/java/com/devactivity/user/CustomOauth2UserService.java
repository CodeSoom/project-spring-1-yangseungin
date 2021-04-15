package com.devactivity.user;

import com.devactivity.repo.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * OAuth2 회원 관련 로직을 처리합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final RepoService repoService;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        User user = save(oAuth2User);

        repoService.createRepository(user);

        user.setStarCount(repoService.getTotalStar(user));

        httpSession.setAttribute("user", user);

        return new DefaultOAuth2User(List.of(new SimpleGrantedAuthority("ROLE_USER"))
                , oAuth2User.getAttributes()
                , userNameAttributeName);
    }

    /**
     * 유저를 저장하고 저장된 유저를 반환합니다.
     *
     * @param oAuth2User 저장할 OAuth 유저
     * @return 저장된 유저
     */
    private User save(OAuth2User oAuth2User) {
        User user = userRepository.findByEmail(oAuth2User.getAttribute("email"))
                .map(user1 -> user1.update(oAuth2User.getAttribute("name"), oAuth2User.getAttribute("login"),
                        oAuth2User.getAttribute("email"), oAuth2User.getAttribute("avatar_url"),
                        oAuth2User.getAttribute("html_url"), oAuth2User.getAttribute("blog"), oAuth2User.getAttribute("repos_url"),
                        oAuth2User.getAttribute("public_repos"), oAuth2User.getAttribute("public_gists"),
                        oAuth2User.getAttribute("followers"), oAuth2User.getAttribute("following"), 0))
                .orElse(
                        User.builder()
                                .name(oAuth2User.getAttribute("name"))
                                .login(oAuth2User.getAttribute("login"))
                                .email(oAuth2User.getAttribute("email"))
                                .avatarUrl(oAuth2User.getAttribute("avatar_url"))
                                .htmlUrl(oAuth2User.getAttribute("html_url"))
                                .blogUrl(oAuth2User.getAttribute("blog"))
                                .reposUrl(oAuth2User.getAttribute("repos_url"))
                                .publicRepos(oAuth2User.getAttribute("public_repos"))
                                .publicGists(oAuth2User.getAttribute("public_gists"))
                                .followers(oAuth2User.getAttribute("followers"))
                                .following(oAuth2User.getAttribute("following"))
                                .starCount(0)
                                .rssUrl("")
                                .bio("")
                                .build()
                );
        return userRepository.save(user);
    }
}
