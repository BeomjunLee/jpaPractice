package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.orderQuery.OrderQueryDTO;
import jpabook.jpashop.domain.enumClass.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.orderQuery.OrderQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    
    //재사용성 높음
    @GetMapping("/v1/orders")
    public Result ordersV1() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDTO> result = orders.stream().map(order -> new SimpleOrderDTO(order)).collect(Collectors.toList());
        return new Result(result);
    }

    //성능이 좋음
    @GetMapping("/v2/orders")
    public Result ordersV2() {
        List<OrderQueryDTO> orderQueryDTO = orderQueryRepository.findOrderDTO();
        return new Result(orderQueryDTO);
    }

    @Data
    static class SimpleOrderDTO {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getOrderStatus();
            this.address = order.getDelivery().getAddress();
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}

