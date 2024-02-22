package com.free.market.item.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotBlank
    private String content;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @NotBlank(message = "판매여부를 체크해주세요.")
    private String open;

    private Long userId;

    private Long createUser;

    private Long updateUser;

    private String createDate;

    private String updateDate;

    public Item toItem() {
        return new Item(id, itemName, content, price, quantity, open, userId, updateDate, updateUser);
    }
}
