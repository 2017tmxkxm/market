package com.free.market.file.service;

import com.free.market.file.domain.FileRequest;
import com.free.market.file.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileMapper fileMapper;

    @Transactional
    public void saveFile(Long itemId, List<FileRequest> files) {
        if(CollectionUtils.isEmpty(files)) {
            return;
        }

        for (FileRequest file : files) {
            file.setItemId(itemId);
        }

        fileMapper.saveAll(files);
    }
}
