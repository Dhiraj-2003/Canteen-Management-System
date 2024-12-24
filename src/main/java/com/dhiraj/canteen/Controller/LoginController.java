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
    public String showLogIn(){
        GlobalData.cart.clear();
        return "/login2";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model theModel){
        theModel.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("user")User theUser, HttpServletRequest request){
        theUser.setActive(1);
        String password=theUser.getPassword();
        Role role= new Role("ROLE_User", theUser.getEmail().toLowerCase());
        theUser.addRole(role);
        theUser.setPassword("{noop}"+password);
        //System.out.println(theUser.toString());
        //saving the new user data
        theUserService.addUser(theUser);
        //instead of directing user to login again we are loging in the user from our side and directing him to shops page

        try {

            request.login(theUser.getEmail().toLowerCase(),password);
        } catch (ServletException e) {
            System.out.println("Error while login "+ e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/forgotpassword")
    public String showForgotPasswordPage(Model model){
        User user=new User();
        model.addAttribute("User",user);
        return "forgotpassword";
    }

    @PostMapping("/forgotpassword")
    public String updateUserPassword(@ModelAttribute("User")User theUser){
        String page="";
        User user = theUserService.getUserByEmail(theUser.getEmail());
        if(user !=null){
            if(theUser.getEmail().equals(user.getEmail()) && theUser.getMobnum().equals(user.getMobnum())){
                user.setPassword("{noop}"+theUser.getPassword());
                theUserService.addUser(user);
                //page="update-success";
                page="redirect:/login";
            }
        }else{
            System.out.println("User is null");
        }

        if(page.isEmpty()){
            return "errors/validcred";
        }
        return page;
    }
}
