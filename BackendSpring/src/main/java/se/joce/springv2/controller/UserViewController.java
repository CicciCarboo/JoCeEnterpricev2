package se.joce.springv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.joce.springv2.model.User;
import se.joce.springv2.service.UserServiceImpl;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static se.joce.springv2.security.UserRole.ADMIN;

@Controller
@RequestMapping("/myTodoList")
public class UserViewController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserViewController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/getUserPage/{id}")
    private String getUser(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userServiceImpl.getUserByID(id));
        return "user";
    }

    @GetMapping("/showFormAddUser")
    public String showFormAddUser(Model model, User user) {
        model.addAttribute("user", user);
        return "add-user-form";
    }

    @PostMapping("/saveUser")
//    @PreAuthorize("hasAuthority('user:write')")
    public String saveUser(@ModelAttribute("user") User user) {

        if (!userServiceImpl.canRegisterNewUser(user)) {
            return "redirect:/myTodoList/invalidEmail";
        }
        return "redirect:/admin/allUsers";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showUpdateUserForm(@PathVariable("id") Integer id, Model model) {

        try {
            User user = userServiceImpl.getUserByID(id);
            model.addAttribute("user", user);
            return "update-user-form";

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user id, exception: " + e);
            return "redirect:/myTodoList/invalidId";
        }
    }

    @PostMapping("/updateUser/{idToUpdate}")
    public String updateUser(@PathVariable("idToUpdate") Integer idToUpdate, User user) {
        userServiceImpl.updateUser(idToUpdate, user);
        return "redirect:/admin/allUsers";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        try {
            userServiceImpl.getUserByID(id);
            String message = userServiceImpl.deleteUserById(id);
            System.out.println("From deleteUser/{id}: " + message);
            return "redirect:/admin/allUsers";

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user id, exception: " + e);
            return "redirect:/myTodoList/invalidId";
        }

    }

    @GetMapping("/invalidId")
    public String getInvalidIdPage() {
        return "invalid-id";
    }

    @GetMapping("/invalidEmail")
    public String getInvalidEmailPage() {
        return "invalid-email";
    }

}

