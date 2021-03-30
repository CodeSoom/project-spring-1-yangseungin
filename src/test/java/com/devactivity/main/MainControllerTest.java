package com.devactivity.main;

import com.devactivity.user.CustomOauth2UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MainController.class)
class MainControllerTest {
    static final String REDIRECT_URL = "http://localhost/";

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("Get / 요청은")
    class Describe_home {

        @Test
        @DisplayName("index.html을 보여주며 상태코드는 200이다.")
        void it_show_index_view() throws Exception {
            mockMvc.perform(get("/"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(content().string(containsString("Sign in with Github")));
        }

        @Nested
        @DisplayName("USER 권한을 가진 사용자는")
        class Context_with_user_role {

            @Test
            @WithMockUser(roles = "USER")
            @DisplayName("본문에 로그아웃을 포함한다.")
            void it_show_index_view() throws Exception {
                mockMvc.perform(get("/"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("index"))
                        .andExpect(content().string(containsString("Logout")));
            }
        }
    }

    @Nested
    @DisplayName("Github 로그인을 시도하면")
    class Describe_login {

        @Test
        @DisplayName("OAuth인증창이 등장한다.")
        void it_show_oauth_view() throws Exception {
            mockMvc.perform(get("/oauth2/authorization/github"))
                    .andDo(print())
                    .andExpect(status().isFound())
                    .andExpect(header().string("Location", containsString("https://github.com/login/oauth/authorize")));
        }
    }

    @Nested
    @DisplayName("GET /user 요청은")
    class Describe_user {

        @Nested
        @DisplayName("USER 권한을 가진 사용자에게")
        class Context_with_user_role {

            @Test
            @WithMockUser(roles = "USER")
            @DisplayName("user.html을 보여주며 상태코드는 200이다.")
            public void it_show_user_view() throws Exception {
                mockMvc.perform(get("/user"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("user"));
            }
        }

        @Nested
        @DisplayName("USER 권한이 없는 사용자에게")
        class Context_without_user_role {

            @Test
            @DisplayName("메인 화면으로 redirect하며 상태코드는 302이다.")
            public void userWithoutUser() throws Exception {
                mockMvc.perform(get("/user"))
                        .andDo(print())
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl(REDIRECT_URL));
            }
        }
    }

    @Nested
    @DisplayName("GET /admin 요청은")
    class Describe_admin {

        @Nested
        @DisplayName("admin 권한을 가진 사용자에게")
        class Context_with_admin_role {

            @Test
            @WithMockUser(roles = "ADMIN")
            @DisplayName("admin.html을 보여주며 상태코드는 200이다.")
            public void it_show_admin_view() throws Exception {
                mockMvc.perform(get("/admin"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(view().name("admin"));
            }
        }

        @Nested
        @DisplayName("ADMIN 권한이 없는 사용자에게")
        class Context_without_admin_role {

            @Test
            @DisplayName("메인 화면으로 redirect하며 상태코드는 302이다.")
            public void userWithoutUser() throws Exception {
                mockMvc.perform(get("/admin"))
                        .andDo(print())
                        .andExpect(status().isFound())
                        .andExpect(redirectedUrl(REDIRECT_URL));
            }
        }
    }
}
