package com.free.market.comment.domain;

import com.free.market.common.dto.SearchDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentSearchDto extends SearchDto {

    private Long itemId;    // 게시글 번호 (FK)
}
