package se.joce.springv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.joce.springv2.model.User;
import se.joce.springv2.repository.UserRepository;
import se.joce.springv2.service.UserServiceImpl;

@Controller
@RequestMapping("/myTodoList")
public class UserViewController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping("/allUsers")
    private String getAllUsersPage(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "all-users";
    }

    @GetMapping("/adminUsers")
    private String getAdminUsers(Model model){
//        get users where user_role is ADMIN
        model.addAttribute("users", userServiceImpl.getAllAdmin());
        return "admin-list";
    }

    @GetMapping("/user")
    private String getUser(Model model, String email) {
        model.addAttribute("users", userRepository.findByEmail(email));
        return "user";
    }


    @GetMapping("/showFormAddUser")
    public String showFormAddUser(Model model, User user){
        model.addAttribute("user", user);
            return "add-user-form";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user){

        if(!userServiceImpl.canRegisterNewUser(user)){
            return "redirect:/myTodoList/invalidEmail";
        }

        return"redirect:/myTodoList/allUsers";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showUpdateUserForm(@PathVariable("id") Integer id, Model model){

        try {
            User user = userServiceImpl.getUserByID(id);
            model.addAttribute("user", user);
            return "update-user-form";

        }catch(IllegalArgumentException e){
            System.out.println("Invalid user id, exception: " + e);
            return "redirect:/myTodoList/invalidId";
        }
    }

    @PostMapping("/updateUser/{idToUpdate}")
    public String updateUser(@PathVariable("idToUpdate") Integer idToUpdate, User user){

        userServiceImpl.updateUser(idToUpdate, user);
        return "redirect:/myTodoList/allUsers";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id){
        try {
            userServiceImpl.getUserByID(id);
            String message = userServiceImpl.deleteUserById(id);
            System.out.println("From deleteUser/{id}: " + message);
            return"redirect:/myTodoList/allUsers";

        }catch(IllegalArgumentException e){
            System.out.println("Invalid user id, exception: " + e);
            return "redirect:/myTodoList/invalidId";
        }


    }

    @GetMapping("/invalidId")
    public String getInvalidIdPage(){
        return "invalid-id";
    }

    @GetMapping("/invalidEmail")
    public String getInvalidEmailPage(){
        return "invalid-email";
    }

    @GetMapping("/admin")
    private String getAdminPage(){
        return "admin";
    }

    @GetMapping("/todo")
    private String getTodoPage(){
        return "todo";
    }
}
