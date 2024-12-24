package com.dhiraj.canteen.Service;


import com.dhiraj.canteen.DAO.CartRepo;
import com.dhiraj.canteen.Entity.Cart;
import com.dhiraj.canteen.Global.GlobalData;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    ProductService productService;

    public void addToCart(Cart cart){
        List<Cart> cartdata= products(cart.getCartEmail());
       boolean isPresent= cartdata.stream().anyMatch(c->c.getProductId()== cart.getProductId());
       if(!isPresent){
           cartRepo.save(cart);
       }
    }

    @Transactional
    public void removeFromCart(Cart cart){
        List<Cart> cartdata=products(cart.getCartEmail());
        int cartId  =  cartdata.stream().filter(c->c.getProductId()==cart.getProductId()).findFirst().get().getId();
        cartRepo.deleteById(cartId);
    }

    public List<Cart> products(String emailparam){
        List<Cart> cartdata=new ArrayList<>();
        if(emailparam==null){
            System.out.println("email passed is null");
        }else{
             cartdata= cartRepo.findByCartEmail(emailparam);
        }
        return cartdata;
    }

    public void getCartItems(){
        GlobalData.findUserName();

        if(GlobalData.user_email !=null && !GlobalData.user_email.equals("anonymousUser") && !GlobalData.user_email.isEmpty()){
            List<Cart> cartdata= products(GlobalData.user_email);
            if(cartdata == null){
                System.out.println("no items found");
            }else{
                cartdata.stream().forEach(
                        e->
                                GlobalData.cart.add(productService.getProductById(e.getProductId()).get())
                );
            }

        }
    }
}
