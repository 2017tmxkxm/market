package com.free.market.file.controller;

import com.free.market.file.domain.FileResponse;
import com.free.market.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileApiController {

    private final FileService fileService;

    @GetMapping("/item/{itemId}/files")
    public List<FileResponse> findAllFileByItemId(@PathVariable(name = "itemId") Long itemId) {
        return fileService.findAllFileByItemId(itemId);
    }
}
