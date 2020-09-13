package jpabook.jpashop.apiService;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.dto.OrderDTO;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderApiService {

    private final OrderRepository orderRepository;

    public List<OrderDTO> findOrderDTO(int offset, int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDTO> result = orders.stream().map(order -> new OrderDTO(order)).collect(Collectors.toList());
        return result;
    }
}
