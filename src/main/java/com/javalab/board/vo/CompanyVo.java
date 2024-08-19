package com.javalab.board.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVo {

    @NotBlank(message = "회사 ID는 필수 입력 항목입니다.")
    @Size(max = 20, message = "회사 ID는 최대 20자까지 입력 가능합니다.")
    private String compId;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    private String password;

    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;

    @NotBlank(message = "회사명은 필수 입력 항목입니다.")
    @Size(max = 50, message = "회사명은 최대 50자까지 입력 가능합니다.")
    private String companyName;

    @NotBlank(message = "사업자 등록번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "\\d{3}-\\d{2}-\\d{5}", message = "사업자 등록번호 형식이 올바르지 않습니다. 예: 123-45-67890")
    private String businessNumber;

    @Pattern(regexp = "^(http|https)://.*$", message = "유효한 URL을 입력하세요.")
    private String homepage;

    private  MultipartFile file;
    private String logoName;
    private String logoPath;

    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    @Size(max = 100, message = "주소는 최대 100자까지 입력 가능합니다.")
    private String address;

    private String status;

    // 권한 관련 필드 추가
    private String userType; // 사용자 유형 (예: 'company')
    private String roleId;   // 권한 ID

}