package com.free.market.file.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FileResponse {

    private Long id;
    private Long itemId;
    private String originalName;
    private String saveName;
    private long size;
    private Boolean deleteYn;
    private LocalDateTime createDate;
    private LocalDateTime deleteDate;
}
