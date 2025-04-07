package funix.epfw.service.order;

import funix.epfw.constants.OrderStatus;
import funix.epfw.model.order.Order;
import funix.epfw.repository.order.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

     @Autowired
    public OrderService(OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }

    public void saveOrder(Order order) {
              orderRepository.save(order);

    }
    public Order findById(Long id) {

        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> findOrdersProductByUser(Long userId) {
         return orderRepository.findOrdersProductByUser(userId);
    }
    public List<Order> findOrdersTourByUser(Long userId) {
         return orderRepository.findOrdersTourByUser(userId);
    }

    public  List<Order> findOrdersByUserId(Long userId) {
         return orderRepository.findOrderByUserId(userId);
    }

    public int countByOrderStatus(OrderStatus orderStatus, Long userId) {
         int productOrder = orderRepository.countByProductsAndOrderStatus(userId, orderStatus);
         int tourOrder = orderRepository.countByToursAndOrderStatus(userId,orderStatus);
        return productOrder + tourOrder;
    }

    public int countOrderProductStatus(OrderStatus orderStatus, Long userId) {
         return orderRepository.countByProductsAndOrderStatus( userId,orderStatus);
    }

    public int countOrderTourStatus(Long userId, OrderStatus orderStatus) {
         return orderRepository.countByToursAndOrderStatus(userId, orderStatus);
    }


   public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist"));

        order.setOrderStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

    }

    public int countOrderUserStatus(Long userId, OrderStatus orderStatus) {
         return orderRepository.countByUserIdAndOrderStatus(userId,orderStatus);
    }

    public void completeOrder(Long orderId) {
         Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist"));
         order.setOrderStatus(OrderStatus.COMPLETED);
         orderRepository.save(order);
    }

}
