package com.free.market.item.service;

import com.free.market.auth.PrincipalDetails;
import com.free.market.common.dto.SearchDto;
import com.free.market.common.paging.Pagination;
import com.free.market.common.paging.PagingResponse;
import com.free.market.item.domain.Item;
import com.free.market.item.domain.ItemSaveForm;
import com.free.market.item.domain.ItemUpdateForm;
import com.free.market.item.mapper.ItemMapper;
import com.free.market.member.domain.MemberResponse;
import com.free.market.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;
    private final MemberMapper memberMapper;

    /**
     * 상품 목록
     * @return List<Item>
     */
    public PagingResponse<Item> findAll(SearchDto params) {
        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null을 담아 반환
        int count = itemMapper.count(params);
        if(count < 1) {
            return new PagingResponse<>(Collections.emptyList(), null);
        }

        // pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<Item> list = itemMapper.findAll(params);
        return new PagingResponse<>(list, pagination);
    }

    /**
     * 상품 등록
     * @param form
     * @return itemId
     */
    public Long save(ItemSaveForm form, Long id) {
        Item item = form.toItem();

        item.setMemberId(id);
        item.setCreateUser(id);
        itemMapper.save(item);

        return item.getId();
    }

    /**
     * 상품 상세
     * @param id
     * @return Item
     */
    public Item findById(Long id) {
        return itemMapper.findById(id);
    }

    /**
     * 상품 삭제
     * @param id
     */
    public void delete(Long id) {
        itemMapper.delete(id);
    }

    /**
     * 상품 수정
     * @param form
     * @return itemId
     */
    public Long update(ItemUpdateForm form, Long id) {
        Item updateItem = form.toItem();
        updateItem.setUpdateUser(id);
        itemMapper.update(updateItem);
        return updateItem.getId();
    }
}
