package com.free.market.file.controller;

import com.free.market.common.file.FileUtils;
import com.free.market.file.domain.FileResponse;
import com.free.market.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileApiController {

    private final FileService fileService;
    private final FileUtils fileUtils;

    // 파일 리스트 조회
    @GetMapping("/item/{itemId}/files")
    public List<FileResponse> findAllFileByItemId(@PathVariable(name = "itemId") Long itemId) {
        return fileService.findAllFileByItemId(itemId);
    }

    // 첨부파일 다운로드
    @GetMapping("/item/{itemId}/files/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "itemId") Long itemId, @PathVariable(name = "fileId") Long fileId) {
        FileResponse file = fileService.findFileById(fileId);
        Resource resource = fileUtils.readFileAsResource(file);

        try {
            String filename = URLEncoder.encode(file.getOriginalName(), "UTF-8");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + filename + "\";")
                    .header(HttpHeaders.CONTENT_LENGTH, file.getSize() + "")
                    .body(resource);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("filename encoding failed : " + file.getOriginalName());
        }
    }
}
