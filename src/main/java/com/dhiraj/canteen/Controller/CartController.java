package com.dhiraj.canteen.Controller;

import com.dhiraj.canteen.DTO.OrderDTO;
import com.dhiraj.canteen.Entity.*;
import com.dhiraj.canteen.Global.GlobalData;
import com.dhiraj.canteen.Service.CartService;
import com.dhiraj.canteen.Service.OrderService;
import com.dhiraj.canteen.Service.ProductService;
import com.dhiraj.canteen.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id) {
        GlobalData.findUserName();

        Cart cartItem = new Cart();
        cartItem.setCartEmail(GlobalData.user_email);
        cartItem.setProductId(id);

        cartService.addToCart(cartItem);
        productService.getProductById(id).ifPresent(product -> GlobalData.cart.add(product));

        return "redirect:/shop";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        GlobalData.cart.clear();
        GlobalData.findUserName();
        cartService.loadCartItems();

        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPprice).sum());
        model.addAttribute("cart", GlobalData.cart);

        return "cart";
    }

    @GetMapping("/cart/removeItem/{index}")
    public String removeItemFromCart(@PathVariable int index) {
        if (index >= 0 && index < GlobalData.cart.size()) {
            Long productId = GlobalData.cart.get(index).getPid();

            Cart cartItem = new Cart();
            cartItem.setProductId(productId);
            cartItem.setCartEmail(GlobalData.user_email);

            GlobalData.cart.remove(index);
            cartService.removeFromCart(cartItem);
        }

        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        User user = userService.getUserByEmail(GlobalData.user_email);

        model.addAttribute("cartCount", GlobalData.cart.size());
        model.addAttribute("total", GlobalData.cart.stream().mapToDouble(Product::getPprice).sum());
        model.addAttribute("amount", user.getAmount());

        return "checkout";
    }

    @GetMapping("/payNow")
    public String payNow(@RequestParam(value = "pickupTime", required = false) String pickupTime, Model model) {
        // Retrieve user details
        User user = userService.getUserByEmail(GlobalData.user_email);

        // Calculate the total cost and remaining balance
        float currentBalance = user.getAmount();
        float totalCost = (float) GlobalData.cart.stream().mapToDouble(Product::getPprice).sum();
        float remainingBalance = currentBalance - totalCost;

        // Check if the user has enough balance to pay
        if (remainingBalance >= 0) {
            // Deduct the total cost from the user's balance
            user.setAmount(remainingBalance);
            userService.saveUser(user);

            // Now create an order for this user with all the cart items
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setUserName(user.getFname() + " " + user.getLname());  // Set the full name of the user
            orderDTO.setTotalPrice((double) totalCost);
            orderDTO.setQuantity(GlobalData.cart.size());  // Assuming each product in the cart is 1 quantity
            orderDTO.setOrderStatus(OrderStatus.PENDING.name());  // Set the initial order status
            orderDTO.setDeliveryTime(LocalDateTime.parse(pickupTime));  // Default delivery time or set based on user's selection

            // Now, create an order in the database using OrderService
            Order savedOrder = orderService.createOrder(orderDTO);

            // Clear the cart after order creation
            GlobalData.cart.clear();

            // Add the order ID as a token for the payment success page
            model.addAttribute("tokenid", savedOrder.getOrderId());
            return "paysuccess";
        }

        // If not enough balance, return to the payment failed page
        return "payfailed";
    }

}
