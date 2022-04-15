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

    @GetMapping("/user")
    private String getUser(Model model, String email) {
        model.addAttribute("users", userRepository.findByEmail(email));
        return "user";
    }


    //Template
//    @PostMapping("/adduser")
//    public String addUser(@Valid User user, BindingResult result, Model model) {
//        if (result.hasErrors()) {
//            return "add-user";
//        }
//
//        userRepository.save(user);
//        return "redirect:/index";
//    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showUpdateUserForm(@PathVariable("id") Integer id, Model model){

        try {
            User user = userServiceImpl.getUserByID(id);
            model.addAttribute("user", user);
            return "update-user";

        }catch(IllegalArgumentException e){
            System.out.println("Invalid user id, exception: " + e);
            return "redirect:/myTodoList/invalidId";
        }
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user){
        if(userServiceImpl.registerNewUser(user)==null){
            return "redirect:/myTodoList/invalidEmail";
        }

        return"redirect:/myTodoList/allUsers";
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
