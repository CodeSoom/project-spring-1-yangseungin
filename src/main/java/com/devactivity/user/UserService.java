package com.devactivity.user;

import com.devactivity.errors.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 유저 관련 로직을 처리합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * username에 해당하는 유저를 반환합니다.
     *
     * @param userName 조회할 유저의 login
     * @return 조회한 유저
     * @throws UserNotFoundException 회원이 존재하지 않는 경우
     */
    public User getUser(String userName) {
        return userRepository.findByLogin(userName)
                .orElseThrow(() -> new UserNotFoundException(userName));
    }

    /**
     * 로그인 유저의 login과 유저의 login이 일치하면 true를, 그렇지 않으면 false를 반환합니다.
     *
     * @param principal  현재 로그인한 유저
     * @param userToView 프로필 유저
     * @return
     */
    public boolean isOwner(OAuth2User principal, User userToView) {
        return userToView.getLogin().
                equals(principal.getAttribute("login"));
    }
}
