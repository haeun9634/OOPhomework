package com.example.oop.dto;

import lombok.*;

public class MemberRequestDto {

    @Getter
    @AllArgsConstructor
    public static class SignRequestDTO{
        private String name;
        private String phoneNumber;
        private Integer birthYear;
        private Integer birthMonth;
        private Integer birthDay;
    }
}
