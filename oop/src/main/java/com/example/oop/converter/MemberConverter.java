package com.example.oop.converter;

import com.example.oop.dto.MemberRequestDto;
import com.example.oop.entity.Member;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class MemberConverter {

    public static Member toMember(MemberRequestDto.SignRequestDTO signRequestDTO) {
        LocalDate birthDate = LocalDate.of(
                signRequestDTO.getBirthYear(),
                signRequestDTO.getBirthMonth(),
                signRequestDTO.getBirthDay()
        );

        return Member.builder()
                .name(signRequestDTO.getName())
                .birthDate(birthDate.atStartOfDay())
                .phoneNumber(signRequestDTO.getPhoneNumber())
                .build();
    }
}
