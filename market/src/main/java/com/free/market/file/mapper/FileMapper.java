package com.free.market.file.mapper;

import com.free.market.file.domain.FileRequest;
import com.free.market.file.domain.UploadFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void saveAll(List<FileRequest> files);
}
