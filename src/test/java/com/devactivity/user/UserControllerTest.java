package com.devactivity.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static final String USER_NAME = "yangseungin";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Nested
    @DisplayName("GET /profile/{username} 요청은")
    class Describe_viewProfile {

        @Nested
        @DisplayName("로그인한 사용자가 자신의 프로필을 조회하면")
        class Context_login_user_view_his_or_her_profile {

            @Test
            @WithMockUser(username = USER_NAME, roles = "USER")
            @DisplayName("프로필을 수정할 수 있다.")
            void it_edit_profile() throws Exception {
                mockMvc.perform(get("/profile/{userName}", USER_NAME))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("user/profile"))
                        .andExpect(content().string(containsString("프로필 수정")))
                        .andExpect(authenticated().withUsername("yangseungin"));
            }
        }
    }
}
