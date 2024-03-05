package com.free.market.member.service;

import com.free.market.member.domain.MemberRequest;
import com.free.market.member.domain.MemberResponse;
import com.free.market.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 정보 저장 (회원 가입)
     * @param params - 회원정보
     * @return PK
     */
    @Transactional
    public Long saveMember(MemberRequest params) {
        params.encodingPassword(passwordEncoder);
        memberMapper.save(params);
        return params.getId();
    }

    /**
     * 회원 상세정보 조회
     * @param loginId - UK
     * @return 회원 상세정보
     */
    public MemberResponse findMemberByLoginId(String loginId) {
        return memberMapper.findByLoginId(loginId);
    }

    /**
     * 회원 정보 수정
     * @param params - 회원정보
     * @return PK
     */
    @Transactional
    public Long updateMember(MemberRequest params) {
        params.encodingPassword(passwordEncoder);
        memberMapper.update(params);
        return params.getId();
    }

    /**
     * 회원 정보 삭제 (회원 탈퇴)
     * @param id - PK
     * @return PK
     */
    @Transactional
    public Long deleteMemberById(Long id) {
        memberMapper.deleteById(id);
        return id;
    }

    /**
     * 회원 카운팅 (ID 중복 체크)
     * @param loginId - UK
     * @return 회원 수
     */
    public int countMemberByLoginId(String loginId) {
        return memberMapper.countByLoginId(loginId);
    }
}
