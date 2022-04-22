package se.joce.springv2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import se.joce.springv2.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    private final UserService userServiceImpl;

    public AdminViewController(UserService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/adminPage")
    public String getAdminPage(){
        return "admin";
    }

    @GetMapping("/allUsers")
    public String getAllUsersPage(Model model){
        model.addAttribute("users", userServiceImpl.getAllUsers());
        return "all-users";
    }

    @GetMapping("/adminUsers")
    public String getAdminUsers(Model model){
        model.addAttribute("users", userServiceImpl.getAllAdmin());
        return "admin-list";
    }
}
