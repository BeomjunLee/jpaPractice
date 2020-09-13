package jpabook.jpashop.domain.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.enumClass.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDTO {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDTO> orderItems;

    public OrderDTO(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress();
        orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDTO(orderItem)).collect(Collectors.toList());
    }
}
