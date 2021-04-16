package com.devactivity.user;

import com.devactivity.errors.UserNotFoundException;
import com.devactivity.feed.FeedService;
import com.devactivity.repo.RepoService;
import com.devactivity.user.form.ProfileForm;
import com.github.dozermapper.core.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final String USER_NAME = "yangseungin";
    private static final String NOT_JOIN_USER_NAME = "NotJoinUser";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @MockBean
    private FeedService feedService;

    @MockBean
    private RepoService repoService;

    @MockBean
    private UserRepository userRepository;


    @MockBean
    private Mapper mapper;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .login(USER_NAME)
                .name("seungin yang")
                .reposUrl("")
                .following(0)
                .followers(0)
                .rssUrl("https://giantdwarf.tistory.com/rss")
                .bio("")
                .blogUrl("")
                .build();
    }

    @Nested
    @DisplayName("GET /profile/{username} 요청은")
    class Describe_viewProfile {

        @Nested
        @DisplayName("로그인한 사용자가 자신의 프로필을 조회하면")
        class Context_login_user_view_his_or_her_profile {

            @BeforeEach
            void setUp() {
                given(userService.getUser(USER_NAME)).willReturn(user);
                given(userService.isOwner(any(), eq(user))).willReturn(true);
            }

            @Test
            @DisplayName("프로필을 볼 수 있다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(get("/profile/{userName}", USER_NAME)
                        .with(oauth2Login()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("user/profile"))
                        .andExpect(model().attributeExists("isOwner"))
                        .andExpect(content().string(containsString("프로필 수정")));
            }
        }

        @Nested
        @DisplayName("가입한적이 없는 사용자의 프로필을 조회하면")
        class Context_view_profile_of_user_who_has_never_signed_up {

            @BeforeEach
            void setUp() {
                given(userService.getUser(NOT_JOIN_USER_NAME)).willThrow(new UserNotFoundException(NOT_JOIN_USER_NAME));
            }

            @Test
            @DisplayName("에러 페이지를 보여준다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(get("/profile/{userName}", NOT_JOIN_USER_NAME))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(view().name("error/user-not-found"))
                        .andExpect(content().string(containsString("없는 유저입니다.")));
            }
        }
    }

    @Nested
    @DisplayName("GET /profileedit 요청은")
    class Describe_viewProfileUpdateForm {

        @Nested
        @DisplayName("로그인한 사용자가 자신의 프로필을 수정하면")
        class Context_login_user_edit_his_or_her_profile {

            @BeforeEach
            void setUp() {
                given(userService.getUser(any())).willReturn(user);
                given(mapper.map(user, ProfileForm.class)).willReturn(new ProfileForm());

            }

            @Test
            @DisplayName("프로필을 수정 뷰를 볼 수 있다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(get("/profileedit")
                        .with(oauth2Login()))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("user/profile-edit"))
                        .andExpect(model().attributeExists("user"))
                        .andExpect(model().attributeExists("profileForm"))
                        .andExpect(content().string(containsString("수정하기")));
            }
        }

        @Nested
        @DisplayName("가입한적이 없는 사용자의 프로필을 수정하려하면")
        class Context_edit_profile_of_user_who_has_never_signed_up {
            @BeforeEach
            void setUp() {
                given(userService.getUser(any())).willThrow(new UserNotFoundException(NOT_JOIN_USER_NAME));
            }

            @Test
            @DisplayName("에러 페이지를 보여준다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(get("/profileedit")
                        .with(oauth2Login()))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(view().name("error/user-not-found"))
                        .andExpect(content().string(containsString("없는 유저입니다.")));
            }
        }
    }

    @Nested
    @DisplayName("POST /profileedit 요청은")
    class Describe_viewProfileUpdate {

        @Nested
        @DisplayName("로그인한 사용자가 자신의 프로필을 수정하면")
        class Context_login_user_edit_his_or_her_profile {

            @BeforeEach
            void setUp() {
                given(userService.getUser(any())).willReturn(user);
            }

            @Test
            @DisplayName("프로필을 수정한다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(post("/profileedit")
                        .with(oauth2Login())
                        .param("bio", "안녕하세요")
                        .param("rssUrl", "https://giantdwarf.tistory.com/rss"))
                        .andDo(print())
                        .andExpect(status().is3xxRedirection())
                        .andExpect(redirectedUrl("/profile/" + user.getLogin()));
            }
        }

        @Nested
        @DisplayName("가입한적이 없는 사용자의 프로필을 수정하려하면")
        class Context_edit_profile_of_user_who_has_never_signed_up {
            @BeforeEach
            void setUp() {
                given(userService.getUser(any())).willThrow(new UserNotFoundException(NOT_JOIN_USER_NAME));
            }

            @Test
            @DisplayName("에러 페이지를 보여준다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(get("/profileedit")
                        .with(oauth2Login()))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(view().name("error/user-not-found"))
                        .andExpect(content().string(containsString("없는 유저입니다.")));
            }
        }
    }
}
