package funix.epfw.repository.order;

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


    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN o.products p " +
            "JOIN p.farm f " +
            "WHERE f.user.id = :userId")
    List<Order> findOrdersByUserId( @Param("userId") Long userId);

}
