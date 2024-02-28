package com.free.market.file.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class FileResponse {

    private Long id;
    private Long itemId;
    private String originalName;
    private String saveName;
    private long size;
    private Boolean deleteYn;
    private LocalDateTime createdDate;
    private LocalDateTime deletedDate;
}
