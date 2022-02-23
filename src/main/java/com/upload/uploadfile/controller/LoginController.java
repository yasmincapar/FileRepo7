package com.upload.uploadfile.controller;

import com.upload.uploadfile.model.Role;
import com.upload.uploadfile.model.User;
import com.upload.uploadfile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserRepository repository;

    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model, HttpSession session) {
        User foundUser = repository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (foundUser != null) {
            session.setAttribute("user", foundUser);
            model.addAttribute("user", foundUser);
            if (foundUser.getRole().equals(Role.ADMIN)) {
                return "redirect:/index";
            } else {
                return "redirect:/files";
            }
        } else {
            return "redirect:/login-error";
        }
    }

    @GetMapping("/login-error")
    public String loginError(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("loginError", true);
        return "login";
    }


}
