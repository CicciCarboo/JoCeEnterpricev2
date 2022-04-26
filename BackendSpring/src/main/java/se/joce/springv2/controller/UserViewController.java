package se.joce.springv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.joce.springv2.model.User;
import se.joce.springv2.service.UserServiceImpl;

@Controller
@RequestMapping("/myTodoList")
public class UserViewController {

//    @Autowired UserServiceImpl userServiceImpl; // It is not recommended to inject field like this. It is better to
//    set the field to private final (limit access to field) and add a constructor to the class. Set @Autowired on
//    the constructor that holds the injected field, if there are several constructors, otherwise @Autowired isn't necessary.

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserViewController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/userPage")
    public String getUser(Model model, String email) {
        model.addAttribute("users", userServiceImpl.getUserByEmail(email));
        return "user";
    }

    //TODO: used this method for user.html body user object - fix!
//    @GetMapping("/getUserPage/{id}")
//    private String getUser(@PathVariable("id") Integer id, Model model) {
//        model.addAttribute("user", userServiceImpl.getUserByID(id));
//        return "user";
//    }


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

        return "redirect:/myTodoList/allUsers";
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
        return "redirect:/myTodoList/allUsers";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        try {
            userServiceImpl.getUserByID(id);
            String message = userServiceImpl.deleteUserById(id);
            System.out.println("From deleteUser/{id}: " + message);
            return "redirect:/myTodoList/allUsers";

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

    @GetMapping("/todo")
    public String getTodoPage() {
        return "todo";
    }

}
