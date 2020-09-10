package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateItemDto {
    String name;
    int price;
    int stockQuantity;
    String author;
    String isbn;
}
