package com.free.market.comment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    private Long id;        // 댓글 번호 (PK)
    private Long itemId;    // 게시글 번호 (FK)
    private String content; // 내용
    private String writer;  // 작성자
}
