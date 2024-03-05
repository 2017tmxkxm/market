package com.free.market.member.domain;

import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.util.StringUtils;

@Getter
public class MemberRequest {

    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private Gender gender;

    public void encodingPassword(PasswordEncoder passwordEncoder) {
        if(StringUtils.isEmpty(password)) {
            return;
        }
        password = passwordEncoder.encode(password);
    }
}
