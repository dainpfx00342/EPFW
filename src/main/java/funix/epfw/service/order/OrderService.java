package funix.epfw.service.order;

import funix.epfw.model.order.Order;
import funix.epfw.repository.order.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
