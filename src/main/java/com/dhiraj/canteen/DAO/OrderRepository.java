package com.dhiraj.canteen.DAO;

import com.dhiraj.canteen.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    public abstract List<Order> findByOrderIdOrUserEmailContainingOrProductPnameContainingOrOrderStatusContaining(
            Long orderId, String userEmail, String productPname, String orderStatus);
    @Query("SELECT o FROM Order o WHERE CAST(o.orderId AS string) LIKE %:keyword%")
    List<Order> searchByOrderIdLike(@Param("keyword") String keyword);
}
