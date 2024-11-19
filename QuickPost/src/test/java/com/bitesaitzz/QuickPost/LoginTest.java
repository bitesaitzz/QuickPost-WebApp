package com.bitesaitzz.QuickPost;


import com.bitesaitzz.QuickPost.controllers.AuthController;
import com.bitesaitzz.QuickPost.models.Person;
import com.bitesaitzz.QuickPost.services.PersonService;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@NoArgsConstructor(force = true)
public class LoginTest {

    @Autowired
    private final MockMvc mockMvc;
    @Autowired
    private final AuthController authController;
    @Autowired
    private final PersonService personService;




    @Test
    public void accessDenied() throws Exception {
        mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }

    @Test
    @WithMockUser
    public void testLoginPost() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl("/auth/login").user("bitesait").password("q"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/messages"));
    }
    @Test
    @WithMockUser
    public void fakeTestLoginPost() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl("/auth/login").user("bitesait").password("qq"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login?error"));
    }

    @Test
    @WithMockUser(username = "test", roles = "USER")
    public void loginByRole() throws Exception {
        mockMvc.perform(formLogin().loginProcessingUrl("/auth/login").user("test").password("test"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/messages"));
        mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", roles = "BANNED")
    public void loginByRole2() throws Exception {

        mockMvc.perform(formLogin().loginProcessingUrl("/auth/login").user("test").password("test"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/messages"));
        mockMvc.perform(get("/messages"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }



}
