package jpabook.jpashop.repository.orderQuery2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.enumClass.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderQueryDTO2 {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDTO> orderItems;

    public OrderQueryDTO2(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
