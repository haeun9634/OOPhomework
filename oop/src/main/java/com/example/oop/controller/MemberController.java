package com.example.oop.controller;

import com.example.oop.dto.MemberRequestDto;
import com.example.oop.entity.Member;
import com.example.oop.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public Member addMember(@RequestBody MemberRequestDto.SignRequestDTO signRequestDTO) {
        return memberService.joinMember(signRequestDTO);
    }
}
