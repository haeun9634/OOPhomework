package com.example.oop.controller;

import com.example.oop.dto.MemberRequestDto;
import com.example.oop.entity.Member;
import com.example.oop.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public Member addMember(@RequestBody MemberRequestDto.SignRequestDTO signRequestDTO) {
        return memberService.joinMember(signRequestDTO);
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }
}
