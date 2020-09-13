package jpabook.jpashop.repository.orderQuery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.enumClass.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderQueryDTO {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public OrderQueryDTO(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
            this.orderId = orderId;
            this.name = name;
            this.orderDate = orderDate;
            this.orderStatus = orderStatus;
            this.address = address;
        }

}
