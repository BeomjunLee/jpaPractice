package jpabook.jpashop.repository.orderQuery2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository2 {

    private final EntityManager em;
    
    //Order DTO 조회 쿼리(모든 관계)
    public List<OrderQueryDTO2> findOrderQueryDTO() {
        List<OrderQueryDTO2> result = findOrders(); //일대다 관계만 빼고 조회한 후에

        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        //인쿼리로 조회
        List<OrderItemQueryDTO> orderItems = em.createQuery(
                "select new jpabook.jpashop.repository.orderQuery2.OrderItemQueryDTO(oi.order.id , oi.item.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDTO.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        //orderId를 기준으로해서 Map으로 바꾸는것
        Map<Long, List<OrderItemQueryDTO>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDTO::getOrderId));

        //orderId를 기준으로 setOrderItems에다가 메모리에 Map을 올려둠
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }


    //Order DTO 조회 쿼리(일대다관계는 빼고)
    private List<OrderQueryDTO2> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.orderQuery2.OrderQueryDTO2(o.id, m.name, o.orderDate, o.orderStatus, d.address) " +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDTO2.class)
                .getResultList();
    }
}
