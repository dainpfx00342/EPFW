package funix.epfw.repository.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.products p " +
            "JOIN p.farm f " +
            "WHERE f.id = :farmId")
    List<Order> findOrdersByFarmId( @Param("farmId") Long farmId);


    @Query("SELECT DISTINCT  o FROM Order o " +
    "JOIN o.products p "+
    "JOIN p.farm f "+
    "WHERE f.user.id = :userId")
    List<Order> findOrdersProductByUser(@Param("userId") Long userId);


    @Query("SELECT DISTINCT o FROM Order o "+
    "JOIN o.tours t "+
    "JOIN t.farm f "+
    "WHERE f.user.id = :userId")
    List<Order> findOrdersTourByUser(@Param("userId") Long userId);



    List<Order> findOrderByUserId( Long userId);


    // Count orders by order status
    int countByOrderStatus(OrderStatus status);

    @Query("SELECT COUNT(DISTINCT o) FROM Order o " +
            "JOIN o.products p " +
            "WHERE o.orderStatus = :status")
    int countByProductsAndOrderStatus(@Param("status") OrderStatus orderStatus);


    @Query("SELECT COUNT(DISTINCT o) FROM Order o " +
            "JOIN o.tours t " +
            "WHERE o.orderStatus = :orderStatus")
    int countByToursAndOrderStatus(OrderStatus orderStatus);

   }
