package com.free.market.item.mapper;


import com.free.market.item.domain.Item;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ItemMapper {

    public List<Item> findAll();
    public void save(Item item);
    public Item findById(Long id);
    public void delete(Long id);

    public void update(Item item);
}
