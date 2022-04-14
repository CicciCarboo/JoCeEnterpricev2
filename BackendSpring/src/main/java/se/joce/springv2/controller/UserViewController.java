package se.joce.springv2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.joce.springv2.repository.UserRepository;

@Controller
@RequestMapping("/myTodoList")
public class UserViewController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/allUsers")
    private String getAllUsersPage(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "allUsers";
    }

    @GetMapping("/user")
    private String getUser(Model model, String email) {
        model.addAttribute("users", userRepository.findByEmail(email));
        return "user";
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
