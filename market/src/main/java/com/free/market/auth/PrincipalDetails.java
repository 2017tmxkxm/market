package com.free.market.auth;

import com.free.market.member.domain.MemberRequest;
import com.free.market.member.domain.MemberResponse;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
// 사용자의 정보를 담는 인터페이스
public class PrincipalDetails implements UserDetails {
    private final MemberResponse memberResponse;

    // 일반 로그인
    public PrincipalDetails(MemberResponse memberResponse) {
        this.memberResponse = memberResponse;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getPassword() {
        return memberResponse.getPassword();
    }

    @Override
    public String getUsername() {
        return memberResponse.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
