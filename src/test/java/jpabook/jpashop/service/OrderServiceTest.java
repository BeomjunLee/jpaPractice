package jpabook.jpashop.service;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.enumClass.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired EntityManager em;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMember();

        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getOrderStatus()); //상품 주문시 상태는 ORDER
        assertEquals(1, getOrder.getOrderItems().size()); //주문한 상품 종류 수가 정확해야 한다
        assertEquals(10000 * orderCount, getOrder.getTotalPrice()); //주문 가격은 가격 * 수량
        assertEquals(8, book.getStockQuantity());//주문 수량만큼 재고가 줄어야한다
    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getOrderStatus()); //주문시 상태는 CANCEL
        assertEquals(10, book.getStockQuantity()); //주문이 취소된 상품은 그만큼 재고가 증가해야함


    }
    
    @Test
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();

        Book book = createBook("JPA", 10000, 10);

        int orderCount = 11;

        //when
        NotEnoughStockException notEnoughStockException = assertThrows(NotEnoughStockException.class,() -> orderService.order(member.getId(), book.getId(), orderCount));

        //then
        assertEquals("재고가 없습니다.", notEnoughStockException.getMessage());

    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강남", "123-123"));
        em.persist(member);
        return member;
    }

}