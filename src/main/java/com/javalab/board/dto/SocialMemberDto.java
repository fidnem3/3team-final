package com.javalab.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class SocialMemberDto extends User implements OAuth2User {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String name;
    private String email;
    private int point = 0; // 포인트 점수 필드 기본 값 설정
    private boolean del = false; // 기본 값 설정
    private boolean social = true; // 기본 값 설정
    private List<String> roles; // 권한 리스트
    private Map<String, Object> attributes; // 소셜 로그인 정보

    public SocialMemberDto(String username, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        super(username, password, authorities);
        this.memberId = username;
        this.roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return this.memberId;
    }
}
