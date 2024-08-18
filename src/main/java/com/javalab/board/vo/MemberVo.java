package com.javalab.board.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
@ToString
public class MemberVo extends User implements Serializable, OAuth2User {
	private static final long serialVersionUID = 1L;

	private String memberId;
	private String password;
	private String name;
	private String email;
	private int point = 0;
	private Collection<SimpleGrantedAuthority> roles = new ArrayList<>();
	private boolean del = false;
	private boolean social = false;
	private Map<String, Object> attributes = Map.of();

	public MemberVo(String memberId, String password, String name, String email, int point,
					Collection<SimpleGrantedAuthority> roles, boolean del, boolean social, Map<String, Object> attributes) {
		super(memberId, password, new ArrayList<>(roles)); // Convert Collection to List
		this.memberId = memberId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.point = point;
		this.roles = roles;
		this.del = del;
		this.social = social;
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
