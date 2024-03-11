package com.free.market.item.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
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

    private Long memberId;

    private Long createUser;

    private Long updateUser;

    private String createDate;

    private String updateDate;

    private List<MultipartFile> files;

    private List<Long> removeFileIds;  // 삭제할 첨부파일 id List

    public Item toItem() {
        return new Item(id, itemName, content, price, quantity, open, updateDate, updateUser);
    }
}
