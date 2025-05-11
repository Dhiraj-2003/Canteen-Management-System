package com.dhiraj.canteen.Service;

import com.dhiraj.canteen.DAO.ProductRepo;
import com.dhiraj.canteen.DAO.UserRepo;
import com.dhiraj.canteen.DTO.OrderDTO;
import com.dhiraj.canteen.Entity.Order;
import com.dhiraj.canteen.Entity.OrderStatus;
import com.dhiraj.canteen.DAO.OrderRepository;
import com.dhiraj.canteen.Entity.Product;
import com.dhiraj.canteen.Entity.User;
import com.dhiraj.canteen.Global.GlobalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProductRepo productRepo;

    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
    }

    public Order createOrder(OrderDTO orderDTO) {
        // Fetch User and Product by their respective fields (email or ID)
        User user = userRepo.findUserByEmail(GlobalData.user_email); // Assuming user is identified by email

        Order order = new Order();
        order.setUser(user);  // Set the fetched user
        order.setQuantity(orderDTO.getQuantity());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setOrderStatus(OrderStatus.PENDING);  // Initial status
        order.setDeliveryTime(orderDTO.getDeliveryTime()); // Set the desired delivery time

        // Set product details (add all products in the cart to the order)
        for (Product product : GlobalData.cart) {
            order.setProduct(product);
        }
        return orderRepository.save(order);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserName(order.getUser().getFname() + " " + order.getUser().getLname());
        dto.setProductName(order.getProduct().getPname());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setOrderStatus(order.getOrderStatus().name());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setDeliveryTime(order.getDeliveryTime()); // Include delivery time in DTO
        return dto;
    }

    public List<Order> searchOrders(String keyword) {
        // Try to convert the keyword to a Long for orderId search
        Long orderId = null;
        try {
            orderId = Long.parseLong(keyword);  // Convert the keyword to a Long (for orderId)
        } catch (NumberFormatException e) {
            // If it is not a valid Long, continue with other search criteria
        }

        // Search by orderId, user email, product name, or order status
        return orderRepository.findByOrderIdOrUserEmailContainingOrProductPnameContainingOrOrderStatusContaining(
                orderId, keyword, keyword, keyword);
    }

    public List<Order> searchByOrderIdLike(String keyword) {
        return orderRepository.searchByOrderIdLike(keyword);
    }

    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }
}
