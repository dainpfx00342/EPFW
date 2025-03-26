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
    @Transactional
    public void saveOrder(Order order) {
              orderRepository.save(order);

    }

    public Order findById(Long id) {

        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrderById(Long id) {

        orderRepository.deleteById(id);
    }

    public List<Order> findAllByFarmId(Long farmId) {
        return orderRepository.findOrdersByFarmId(farmId);
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

    public int countByOrderStatus(OrderStatus orderStatus) {
        return orderRepository.countByOrderStatus(orderStatus);
    }

    public int countOrderProductStatus(OrderStatus orderStatus) {
         return orderRepository.countByProductsAndOrderStatus(orderStatus);
    }

    public int countOrderTourStatus(OrderStatus orderStatus) {
         return orderRepository.countByToursAndOrderStatus(orderStatus);
    }


    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist"));

        order.setOrderStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        System.out.println("✅ Đã xác nhận đơn hàng ID: " + orderId);
    }
}
