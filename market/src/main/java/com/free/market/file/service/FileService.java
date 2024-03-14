package com.free.market.file.service;

import com.free.market.file.domain.FileRequest;
import com.free.market.file.domain.FileResponse;
import com.free.market.file.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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

    public List<FileResponse> findAllFileByItemId(Long itemId) {
        return fileMapper.findAllByItemId(itemId);
    }

    public List<FileResponse> findAllFileByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        return fileMapper.findAllByIds(ids);
    }

    @Transactional
    public void deleteAllFileByIds(List<Long> ids) {
        if(CollectionUtils.isEmpty(ids)) {
            return;
        }

        fileMapper.deleteAllByIds(ids);
    }

    /**
     * 파일 상세 정보 조회
     * @param id - PK
     * @return 파일 상세정보
     */
    public FileResponse findFileById(Long id) {
        return fileMapper.findById(id);
    }
}
