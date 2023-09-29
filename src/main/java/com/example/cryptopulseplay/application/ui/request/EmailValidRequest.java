package com.example.cryptopulseplay.application.ui.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailValidRequest {


    @NotNull(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식을 잘 입력해주세요")
    public String email;




}
