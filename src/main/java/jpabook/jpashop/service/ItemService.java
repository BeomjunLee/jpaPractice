package jpabook.jpashop.service;

import jpabook.jpashop.controller.UpdateItemDTO;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    
    //상품 등록
    @Transactional
    public Long saveItem(Item item) {
        itemRepository.save(item);
        return item.getId();
    }

    //상품 수정
    @Transactional
    public void updateItem(Long itemId, UpdateItemDTO itemDto) {
        Book findItem = (Book)itemRepository.findOne(itemId);
        findItem.setName(itemDto.getName());
        findItem.setPrice(itemDto.getPrice());
        findItem.setStockQuantity(itemDto.getStockQuantity());
        findItem.setAuthor(itemDto.getAuthor());
        findItem.setIsbn(itemDto.getIsbn());
    }
    
    //상품목록 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    //상품 조회
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
