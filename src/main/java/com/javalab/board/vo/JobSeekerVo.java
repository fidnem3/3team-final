package com.javalab.board.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;


import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class JobSeekerVo {
    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,}$", message = "아이디는 영문 및 숫자를 포함하여 5자 이상이어야 합니다.")
    private String jobSeekerId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{5,}$", message = "비밀번호는 대소문자, 숫자 및 특수문자를 포함하여 5자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String confirmPassword; // 비밀번호 확인 필드 추가

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[가-힣A-Za-z]{2,}$", message = "이름은 한글 또는 영문으로 두 글자 이상이어야 합니다.")
    private String name;

    @NotNull(message = "생년월일은 필수 입력 값입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 '-'를 제외한 10자리 또는 11자리 숫자만 입력해야 합니다.")
    private String tel;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식을 입력해 주세요.")
    private String email;

    private String fileName;
    private String filePath;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    //권한 USER
    private String role;
}