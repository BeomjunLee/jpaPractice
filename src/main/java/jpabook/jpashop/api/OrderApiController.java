package jpabook.jpashop.api;
import jpabook.jpashop.apiService.OrderApiService;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.dto.OrderDTO;
import jpabook.jpashop.domain.enumClass.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.orderQuery2.OrderQueryRepository2;
import jpabook.jpashop.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api2")
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository2 orderQueryRepository2;
    private final OrderApiService orderApiService;

    //batchSize 이용
    //엔티티를 -> DTO로 변환조회
    @GetMapping("/v1/orders")
    public ResponseEntity ordersV1(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
//        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
//        List<OrderDTO> result = orders.stream().map(order -> new OrderDTO(order)).collect(Collectors.toList());
        List<OrderDTO> result = orderApiService.findOrderDTO(offset, limit);
        return ResponseEntity.ok().body(new Result(result));
    }

    //DTO로 조회
    @GetMapping("/v2/orders")
    public Result ordersV2() {
        return new Result(orderQueryRepository2.findOrderQueryDTO());
    }

//    @Data
//    static class OrderDTO {
//        private Long orderId;
//        private String name;
//        private LocalDateTime orderDate;
//        private OrderStatus orderStatus;
//        private Address address;
//        private List<OrderItemDTO> orderItems;
//
//        public OrderDTO(Order order) {
//            orderId = order.getId();
//            name = order.getMember().getName();
//            orderDate = order.getOrderDate();
//            orderStatus = order.getOrderStatus();
//            address = order.getDelivery().getAddress();
//            orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDTO(orderItem)).collect(Collectors.toList());
//        }
//    }
//
//    @Data
//    static class OrderItemDTO {
//        private String name;
//        private int orderPrice;
//        private int count;
//
//        public OrderItemDTO(OrderItem orderItem) {
//            name = orderItem.getItem().getName();
//            orderPrice = orderItem.getOrderPrice();
//            count = orderItem.getCount();
//        }
//    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }


}
