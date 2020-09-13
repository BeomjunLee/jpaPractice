package jpabook.jpashop.repository.orderQuery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDTO> findOrderDTO() {
        return em.createQuery("select new jpabook.jpashop.repository.orderQuery.OrderQueryDTO(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                " from Order o join o.member m join o.delivery d", OrderQueryDTO.class).getResultList();
    }
}
