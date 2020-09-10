package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired private ItemService itemService;
    @Autowired private ItemRepository itemRepository;
    
    @Test
    public void 상품등록() throws Exception{
        //given
        Book book = new Book();
        book.setAuthor("이범준");
        book.setIsbn("1000000");
        book.setName("SpringBoot");
        book.setPrice(20000);
        book.addStock(100);

        //when
        Long itemId = itemService.saveItem(book);

        //then
        Assertions.assertEquals(book, itemRepository.findOne(itemId));
    }

}