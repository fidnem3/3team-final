package com.javalab.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class MemberDto extends User {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String name;
    private String email;
    private int point = 0; // 포인트 점수 필드 기본 값 설정
    private boolean del = false; // 기본 값 설정
    private boolean social = false; // 기본 값 설정
    private List<String> roles; // 권한 리스트

    public MemberDto(String username, String password, Collection<? extends GrantedAuthority> authorities,
                     String name, String email, int point, boolean del, boolean social) {
        super(username, password, authorities);
        this.memberId = username;
        this.name = name;
        this.email = email;
        this.point = point;
        this.del = del;
        this.social = social;
        this.roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
