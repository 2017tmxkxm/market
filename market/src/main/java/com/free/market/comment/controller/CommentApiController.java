package com.free.market.comment.controller;

import com.free.market.comment.domain.CommentRequest;
import com.free.market.comment.domain.CommentResponse;
import com.free.market.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    // 신규 댓글 생성
    @PostMapping("/item/{itemId}/comments")
    public CommentResponse saveComment(@PathVariable(name = "itemId") Long itemId, @RequestBody CommentRequest params) {
        Long id = commentService.save(params);
        return commentService.findById(itemId);
    }

    // 댓글 리스트 조회
    @GetMapping("/item/{itemId}/comments")
    public List<CommentResponse> findAllComment(@PathVariable(name = "itemId") Long itemId) {
        return commentService.findAll(itemId);
    }
}
