package com.devactivity.user;

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

@Service
@Transactional
@RequiredArgsConstructor
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
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

        httpSession.setAttribute("user", user);

        return new DefaultOAuth2User(List.of(new SimpleGrantedAuthority("ROLE_USER"))
                , oAuth2User.getAttributes()
                , userNameAttributeName);
    }

    private User save(OAuth2User oAuth2User) {
        User user = userRepository.findByEmail(oAuth2User.getAttribute("email"))
                .orElse(
                        User.builder()
                                .name(oAuth2User.getAttribute("name"))
                                .email(oAuth2User.getAttribute("email"))
                                .avatarUrl(oAuth2User.getAttribute("avatar_url"))
                                .build()
                );
        return userRepository.save(user);
    }
}
