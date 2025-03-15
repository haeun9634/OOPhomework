package com.example.oop.converter;

import com.example.oop.dto.MemberRequestDto;
import com.example.oop.entity.Member;
import com.example.oop.entity.status.MemberStatus;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class MemberConverter {

    public static Member toMember(MemberRequestDto.SignRequestDTO signRequestDTO) {
        return Member.builder()
                .name(signRequestDTO.getName())
                .birthDate(signRequestDTO.getBirthDate().atStartOfDay())
                .phoneNumber(signRequestDTO.getPhoneNumber())
                .memberStatus(MemberStatus.ACTIVE)
                .build();
    }
}
