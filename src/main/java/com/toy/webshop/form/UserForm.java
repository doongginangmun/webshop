package com.toy.webshop.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserForm {

    @Email
    @NotEmpty(message = "회원 이메일은 필수 입니다.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입니다.")
    private String password;

    @NotEmpty(message = "이름은 필수 입니다.")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}
