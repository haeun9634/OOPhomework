package com.example.oop.service;

import com.example.oop.converter.MemberConverter;
import com.example.oop.dto.MemberRequestDto;
import com.example.oop.entity.Member;
import com.example.oop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    // 회원가입
    public Member joinMember(MemberRequestDto.SignRequestDTO signRequestDTO){
        return memberRepository.save(memberConverter.toMember(signRequestDTO));
    }

    // 회원 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

}
