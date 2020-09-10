package jpabook.jpashop.service;

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
    
    //상품목록 조회
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    //상품 조회
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
