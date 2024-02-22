package com.free.market.item.service;

import com.free.market.item.domain.Item;
import com.free.market.item.domain.ItemSaveForm;
import com.free.market.item.domain.ItemUpdateForm;
import com.free.market.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;

    /**
     * 상품 목록
     * @return List<Item>
     */
    public List<Item> findAll() {
        return itemMapper.findAll();
    }

    /**
     * 상품 등록
     * @param form
     * @return itemId
     */
    public Long save(ItemSaveForm form) {
        Item item = form.toItem();
        item.setUserId(1L);
        item.setCreateUser(1L);
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
    public Long update(ItemUpdateForm form) {
        Item updateItem = form.toItem();
        itemMapper.update(updateItem);
        return updateItem.getId();
    }
}
