package com.example.oop.dto;

import lombok.*;

import java.time.LocalDate;

public class MemberRequestDto {

    @Getter
    @AllArgsConstructor
    public static class SignRequestDTO{
        private String name;
        private String phoneNumber;
        private LocalDate birthDate;
    }
}
