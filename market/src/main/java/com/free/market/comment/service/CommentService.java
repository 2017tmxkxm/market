package com.free.market.comment.service;

import com.free.market.comment.domain.CommentRequest;
import com.free.market.comment.domain.CommentResponse;
import com.free.market.comment.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;

    /**
     * 댓글 저장
     * @param params - 댓글 정보
     * @return id - PK
     */
    @Transactional
    public Long save(CommentRequest params) {
        commentMapper.save(params);
        return params.getId();
    }

    /**
     * 댓글 상세정보 조회
     * @param id - PK
     * @return CommentResponse - 댓글 상세정보
     */
    public CommentResponse findById(Long id) {
        return commentMapper.findById(id);
    }

    /**
     * 댓글 수정
     * @param params - 댓글 정보
     * @return id - PK
     */
    @Transactional
    public Long update(CommentRequest params) {
        commentMapper.update(params);
        return params.getId();
    }

    /**
     * 댓글 삭제
     * @param id - PK
     * @return id - PK
     */
    @Transactional
    public Long delete(Long id) {
        commentMapper.deleteById(id);
        return id;
    }

    /**
     * 댓글 리스트 조회
     * @param itemId - 게시글 번호 (PK)
     * @return 특정 게시글에 등록된 댓글 리스트
     */
    public List<CommentResponse> findAll(Long itemId) {
        return commentMapper.findAll(itemId);
    }
}
