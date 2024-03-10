package com.free.market.member.controller;

import com.free.market.member.domain.MemberRequest;
import com.free.market.member.domain.MemberResponse;
import com.free.market.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm(@RequestParam(name = "error", required = false) String error
                            , @RequestParam(name = "exception", required = false) String exception
                            , Model model) {

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "member/login";
    }

    @GetMapping("/add")
    public String add() {
        return "member/addForm";
    }

    // 회원가입
    @PostMapping
    @ResponseBody
    public Long join(@RequestBody MemberRequest params) {
        return memberService.saveMember(params);
    }

    // 회원 조회
    @GetMapping("/{loginId}")
    @ResponseBody
    public MemberResponse findMemberByLoginId(@PathVariable(name = "loginId") String loginId) {
        return memberService.findMemberByLoginId(loginId);
    }

    // 회원 정보 수정
    @PatchMapping("/{id}")
    @ResponseBody
    public Long updateMember(@PathVariable(name = "id") Long id, @RequestBody MemberRequest params) {
        return memberService.updateMember(params);
    }

    // 회원 삭제 (회원탈퇴)
    @DeleteMapping("/{id}")
    @ResponseBody
    public Long deleteMemberById(@PathVariable(name = "id") Long id) {
        return memberService.deleteMemberById(id);
    }

    // 회원 수 카운팅 (ID 중복 체크)
    @GetMapping("/count")
    @ResponseBody
    public int countMemberByLoginId(@RequestParam(name = "loginId") String loginId) {
        return memberService.countMemberByLoginId(loginId);
    }
}
