package com.javalab.board.security.dto;

import com.javalab.board.vo.UserRolesVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

// 인자 없는 기본 생성자를 자동으로 생성
@NoArgsConstructor
// 모든 필드에 대한 getter 메소드를 자동으로 생성
@Getter
// 모든 필드에 대한 setter 메소드를 자동으로 생성
@Setter
public class CustomUserDetails implements UserDetails {
    private String username;  // 사용자 이름 (ID)
    private String password;  // 사용자 비밀번호
    private UserRolesVo userRoles;  // 사용자 역할 정보


    // username, password, userRoles만을 인자로 받는 생성자
    public CustomUserDetails(String username, String password, UserRolesVo userRoles) {
        this.username = username;
        this.password = password;
        this.userRoles = userRoles;
    }

    // 사용자의 권한 목록을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // userRoles의 userType을 대문자로 변환하여 "ROLE_" 접두사를 붙인 권한을 생성
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRoles.getUserType().toUpperCase()));
    }

    // 사용자의 비밀번호 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 사용자의 이름(ID) 반환
    @Override
    public String getUsername() {
        return username;
    }

    // 사용자의 유형 반환 (예: 'company', 'jobSeeker')
    public String getUserType() {
        return userRoles.getUserType();
    }


    // 계정 상태 관련 메서드를 항상 true로 반환
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

