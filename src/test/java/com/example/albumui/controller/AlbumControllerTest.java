package com.example.albumui.controller;

import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${API_BASE_URL:}")
    private String apiBaseUrl;

    @Value("${BACKGROUND_COLOR:#ffffff}")
    private String backgroundColor;

    /**
     * HttpSessionにユーザ名が登録されていない場合
     * @throws Exception
     */
    @Test
    public void indexNoHttpSession() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/login"));
    }

    /**
     * HttpSessionにユーザ名が登録されてる場合
     * @throws Exception
     */
    @Test
    public void indexHttpSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "test");
        mockMvc.perform(get("/").session(session))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }
    

    @Test
    public void loginPost() throws Exception {
        mockMvc.perform(post("/login"))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/"));
    }

    @Test
    public void testLoginGet() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("login"));
    }

    @Test
    public void testLogout() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "test");        
        mockMvc.perform(post("/logout").session(session))
               .andExpect(status().is3xxRedirection())
               .andExpect(view().name("redirect:/login"));

        assertTrue(session.isInvalid());
    }
}