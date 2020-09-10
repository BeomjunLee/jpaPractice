package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateItemDTO {
    String name;
    int price;
    int stockQuantity;
    String author;
    String isbn;
}
