package com.free.market.member.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {

    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private Gender gender;
    private Boolean deleteYn;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public void clearPassword() {
        this.password = "";
    }
}
