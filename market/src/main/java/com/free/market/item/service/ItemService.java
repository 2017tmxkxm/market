package com.free.market.item.service;

import com.free.market.item.domain.Item;
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
     * 아이템 목록
     */
    public List<Item> findAll() {
        return itemMapper.findAll();
    }
}
