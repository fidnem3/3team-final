package com.javalab.board.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
@ToString
//@Builder
public class MemberVo extends User implements Serializable, OAuth2User {
	private static final long serialVersionUID = 1L;

	private String memberId;
	private String password;
	private String name;
	private String email;
	private int point = 0;  // 포인트 점수 필드 기본 값 설정
	private List<String> roles = new ArrayList<>(); // 권한 리스트 기본 값 설정
	private boolean del = false; // 기본 값 설정
	private boolean social = false; // 기본 값 설정
	private Map<String, Object> attributes = Map.of(); // 소셜 로그인 정보 기본 값 설정

	// 기본 생성자
	public MemberVo() {
		super("defaultMemberId", "defaultPassword", List.of(new SimpleGrantedAuthority("ROLE_USER")));
	}

	public MemberVo(String memberId,
					String password,
					String name,
					String email,
					int point,
					List<String> roles,
					boolean del,
					boolean social,
					Map<String, Object> attributes) {

		super(memberId, password, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
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
