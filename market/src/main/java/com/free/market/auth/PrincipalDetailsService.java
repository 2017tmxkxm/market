package com.free.market.auth;

import com.free.market.member.domain.MemberResponse;
import com.free.market.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        MemberResponse byLoginId = memberMapper.findByLoginId(loginId);

        if(byLoginId == null) {
            throw new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. " + loginId);
        } else {
            return new PrincipalDetails(byLoginId);
        }

    }
}
