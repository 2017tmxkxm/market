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
    private Long userId;
    private String createDate;
    private Long createUser;
    private String updateDate;
    private Long updateUser;

}
