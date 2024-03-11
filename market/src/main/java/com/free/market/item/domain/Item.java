package com.free.market.item.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long id;
    private String itemName;
    private String content;
    private Integer price;
    private Integer quantity;
    private String open;
    private Long memberId;
    private String createDate;
    private Long createUser;
    private String updateDate;
    private Long updateUser;
    private Long fileId;

    // ItemSaveForm Item으로 변환시 사용 생성자
    public Item(String itemName, String content, Integer price, Integer quantity, String open, Long memberId, Long createUser) {
        this.itemName = itemName;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        this.open = open;
        this.memberId = memberId;
        this.createUser = createUser;
    }

    // ItemUpdateForm Item으로 변환시 사용 생성자
    public Item(Long id, String itemName, String content, Integer price, Integer quantity, String open, String updateDate, Long updateUser) {
        this.id = id;
        this.itemName = itemName;
        this.content = content;
        this.price = price;
        this.quantity = quantity;
        this.open = open;
        this.updateDate = updateDate;
        this.updateUser = updateUser;
    }
}
