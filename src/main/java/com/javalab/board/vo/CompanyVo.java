package com.javalab.board.vo;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVo {
    private String compId;
    private String password;
    private String email;
    private String companyName;
    private String businessNumber;
    private String homepage;
    private String logoName;
    private String logoPath;
    private String address;
    private String status;
}
