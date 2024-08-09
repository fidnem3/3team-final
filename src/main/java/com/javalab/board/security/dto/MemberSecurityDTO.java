package com.javalab.board.security.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User implements OAuth2User {

    private String id;
    private String password;
    private String email;
    private boolean del;
    private boolean social;

    private Map<String, Object> props; // 소셜 로그인 정보

    public MemberSecurityDTO(String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities) {
        // 상위 객체인 OAuth2User 생성자 호출
        super(username, password, authorities);
        this.id = username;
        this.password = password;
        this.email = username;
//        this.del = del;
//        this.social = social;
    }

    public Map<String, Object> getAttributes() {
        return this.getProps();
    }

    @Override
    public String getName() {
        return this.id;
    }

}
