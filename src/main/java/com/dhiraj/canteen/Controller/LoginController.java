package com.dhiraj.canteen.Controller;

import com.dhiraj.canteen.Entity.Role;
import com.dhiraj.canteen.Entity.User;
import com.dhiraj.canteen.Global.GlobalData;
import com.dhiraj.canteen.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private UserService theUserService;

    @GetMapping("/login")
    public String showLoginPage() {
        GlobalData.cart.clear();
        return "login2";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, HttpServletRequest request) {
        user.setActive(1);
        String rawPassword = user.getPassword();

        Role role = new Role("ROLE_User", user.getEmail().toLowerCase(),user);
        user.addRole(role);
        user.setPassword("{noop}" + rawPassword);

        theUserService.saveUser(user);

        try {
            request.login(user.getEmail().toLowerCase(), rawPassword);
        } catch (ServletException e) {
            System.out.println("Login error: " + e.getMessage());
        }

        return "redirect:/home";
    }

    @GetMapping("/forgotpassword")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("User", new User());
        return "forgotpassword";
    }

    @PostMapping("/forgotpassword")
    public String updateUserPassword(@ModelAttribute("User") User inputUser) {
        User existingUser = theUserService.getUserByEmail(inputUser.getEmail());

        if (existingUser != null &&
                inputUser.getEmail().equals(existingUser.getEmail()) &&
                inputUser.getMobnum().equals(existingUser.getMobnum())) {

            existingUser.setPassword("{noop}" + inputUser.getPassword());
            theUserService.saveUser(existingUser);
            return "redirect:/login";
        }

        return "errors/validcred";
    }
}
