package com.javalab.board.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class SocialMemberDto implements OAuth2User {
    private String memberId;
    private String name;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
    private Map<String, Object> attributes;
    private boolean isSocial; // Add this field to indicate social login

    public SocialMemberDto(String memberId, String name, String email, Collection<? extends GrantedAuthority> roles, Map<String, Object> attributes, boolean isSocial) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.attributes = attributes;
        this.isSocial = isSocial;
    }

    @Override
    public <A> A getAttribute(String name) {
        return this.attributes != null ? (A) this.attributes.get(name) : null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getName() {
        return this.memberId;
    }

    public boolean isSocial() {
        return this.isSocial;
    }
}
