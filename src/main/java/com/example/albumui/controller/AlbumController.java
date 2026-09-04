package com.example.albumui.controller;

import com.example.albumui.model.Album;
import com.example.albumui.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AlbumController {

    private final RestClient restClient = RestClient.create();

    @Value("${API_BASE_URL:}")
    private String apiBaseUrl;

    @Value("${BACKGROUND_COLOR:#ffffff}")
    private String backgroundColor;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        List<Album> albums = List.of();

        if (apiBaseUrl != null && !apiBaseUrl.isBlank()) {
            try {
                String targetUrl = apiBaseUrl.replaceAll("/+$", "") + "/albums";
                albums = restClient.get()
                        .uri(targetUrl)
                        .retrieve()
                        .body(new ParameterizedTypeReference<List<Album>>() {});
            } catch (Exception e) {
                model.addAttribute("error", "APIからのデータ取得に失敗しました: " + e.getMessage());
            }
        }
    
        String ipaddr = getIpAddr();

        String name = (String)session.getAttribute("user");
        User user = new User();
        user.setName(name);

        model.addAttribute("albums", albums);
        model.addAttribute("backgroundColor", backgroundColor);
        model.addAttribute("ipaddr", ipaddr);
        model.addAttribute("user", user);

        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session) {
        session.setAttribute("user", user.getName());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute User user) {
        return "login";
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute User user, HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private String getIpAddr() {
        try {
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (java.net.UnknownHostException e) {
            return "Unknown Host";
        }
    }
}