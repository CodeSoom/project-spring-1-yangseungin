package com.devactivity.user;

import com.devactivity.errors.UserNotFoundException;
import com.devactivity.user.form.ProfileForm;
import com.github.dozermapper.core.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 유저 관련 로직을 처리합니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Mapper mapper;

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
     * @param principal  현재 로그인한 유저저
     * @param userToView 프로필 유저
     * @return
     */
    public boolean isOwner(OAuth2User principal, User userToView) {
        return userToView.getLogin().
                equals(principal.getAttribute("login"));
    }

    /**
     * 유저의 프로필을 수정합니다.
     *
     * @param user        수정할 유저
     * @param profileForm 수정할 프로필 정보
     */
    public void updateProfile(User user, ProfileForm profileForm) {
        mapper.map(profileForm, user);
        userRepository.save(user);
    }

    /**
     * 유저의 rss url을 삭제합니다.
     *
     * @param user 삭제할 유저
     */
    public void deleteRssUrl(User user) {
        user.setRssUrl("");
    }

    public List<User> getTopTenUser() {
        return userRepository.findTop10AllByOrderByStarCountDesc();
    }
}
