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
    private CartRepo cartRepo;

    @Autowired
    private ProductService productService;

    public void addToCart(Cart cart) {
        List<Cart> cartData = getCartItemsByEmail(cart.getCartEmail());
        boolean isPresent = cartData.stream().anyMatch(c -> c.getProductId().equals(cart.getProductId()));
        if (!isPresent) {
            cartRepo.save(cart);
        }
    }

    @Transactional
    public void removeFromCart(Cart cart) {
        List<Cart> cartData = getCartItemsByEmail(cart.getCartEmail());
        cartData.stream()
                .filter(c -> c.getProductId().equals(cart.getProductId()))
                .findFirst()
                .ifPresent(c -> cartRepo.deleteById(c.getId()));
    }

    public List<Cart> getCartItemsByEmail(String email) {
        if (email == null || email.isEmpty()) {
            System.out.println("Email passed is null or empty.");
            return new ArrayList<>();
        }
        return cartRepo.findByCartEmail(email);
    }

    public void loadCartItems() {
        GlobalData.findUserName();

        if (GlobalData.user_email != null && !GlobalData.user_email.equals("anonymousUser") && !GlobalData.user_email.isEmpty()) {
            List<Cart> cartData = getCartItemsByEmail(GlobalData.user_email);
            if (cartData != null && !cartData.isEmpty()) {
                cartData.forEach(e -> GlobalData.cart.add(productService.getProductById(e.getProductId()).orElse(null)));
            } else {
                System.out.println("No items found in cart.");
            }
        }
    }
}
