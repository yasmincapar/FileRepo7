package com.upload.uploadfile.controller;

import com.upload.uploadfile.model.Role;
import com.upload.uploadfile.model.User;
import com.upload.uploadfile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repository;

    @GetMapping("/index")
    public String showUserList(Model theModel, HttpSession session) {

        User user = (User) session.getAttribute("user");

        theModel.addAttribute("users", repository.findAll());
        if (user.getRole().equals(Role.ADMIN)) {
            return "index";//
        } else if (user.getRole().equals(Role.USER)) {
            return "unauthorized";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/signup")
    public String showSignupPage(User user, HttpSession session) {
        User userInSession = (User) session.getAttribute("user");
        if (userInSession.getRole().equals(Role.ADMIN)) {
            return "adduser";
        } else if (userInSession.getRole().equals(Role.USER)) {
            return "unauthorized";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model theModel) {
        if (result.hasErrors()) {
            return "adduser";
        }

        repository.save(user);
        return "redirect:/index";
    }

    @GetMapping("/edit/{id}")
    public String showUpdatePage(@PathVariable int id, Model theModel, HttpSession session) {
        User userInSession = (User) session.getAttribute("user");
        if (userInSession.getRole().equals(Role.ADMIN)) {
            User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));
            theModel.addAttribute("user", user);
            return "updateuser";
        } else if (userInSession.getRole().equals(Role.USER)) {
            return "unauthorized";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable int id, @Valid User user, BindingResult result, Model theModel, HttpSession session) {
        User userInSession = (User) session.getAttribute("user");
        if (userInSession.getRole().equals(Role.ADMIN)) {
            if (result.hasErrors()) {
                return "updateuser";
            }

            repository.save(user);
            return "redirect:/index";
        } else if (userInSession.getRole().equals(Role.USER)) {
            return "unauthorized";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session) {

        User userInSession = (User) session.getAttribute("user");
        if (userInSession.getRole().equals(Role.ADMIN)) {
            User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user : " + id));
            repository.delete(user);

            return "redirect:/index";
        } else if (userInSession.getRole().equals(Role.USER)) {
            return "unauthorized";
        } else {
            return "redirect:/login";
        }
    }

}
