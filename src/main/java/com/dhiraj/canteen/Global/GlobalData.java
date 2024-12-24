package com.dhiraj.canteen.Global;

import com.dhiraj.canteen.Entity.Product;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GlobalData {
    public static List<Product> cart;
    public static String user_email;
    public static Collection<? extends GrantedAuthority> user_role;


   static {
        cart = new ArrayList<>();
       findUserName();
   }
    public static void findUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            GlobalData.user_email = ((UserDetails)principal).getUsername();
            user_role=((UserDetails)principal).getAuthorities();
        } else {
            GlobalData.user_email = principal.toString();
        }
    }
}
