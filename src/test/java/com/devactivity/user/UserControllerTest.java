package com.devactivity.user;

import com.devactivity.errors.UserNotFoundException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
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

    @BeforeEach
    void setup() {
        User user = User.builder()
                .login(USER_NAME)
                .name("seungin yang")
                .build();

        given(userService.getUser(USER_NAME)).willReturn(user);
        given(userService.getUser(NOT_JOIN_USER_NAME)).willThrow(new UserNotFoundException(NOT_JOIN_USER_NAME));
        given(userService.isOwner(any(), eq(user))).willReturn(true);

    }

    @Nested
    @DisplayName("GET /profile/{username} 요청은")
    class Describe_viewProfile {

        @Nested
        @DisplayName("로그인한 사용자가 자신의 프로필을 조회하면")
        class Context_login_user_view_his_or_her_profile {

            @Test
            @DisplayName("프로필을 수정할 수 있다.")
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

            @Test
            @DisplayName("프로필을 수정할 수 있다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(get("/profile/{userName}", NOT_JOIN_USER_NAME))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andExpect(view().name("error/user-not-found"))
                        .andExpect(content().string(containsString("없는 유저입니다.")));
            }
        }
    }
}
