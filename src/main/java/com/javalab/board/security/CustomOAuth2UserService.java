package com.javalab.board.security;

import com.javalab.board.dto.SocialMemberDto;
import com.javalab.board.repository.LoginMapper;
import com.javalab.board.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * [CustomOAuth2UserService 클래스]
 * - DefaultOAuth2UserService 클래스를 상속받아 구현한 클래스
 * - OAuth2UserService 인터페이스를 구현한 CustomOAuth2UserService 빈 등록
 */
@Log4j2
//@Service // OAuth2Config에서 빈으로 등록되므로 주석처리함.
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * [loadUser 메소드]
     * - OAuth2UserRequest 객체를 전달받아 OAuth2User 객체를 반환하는 메소드
     * - OAuth2UserRequest 객체는 사용자의 OAuth2 인증 요청에 대한 정보(AccessToken)를 포함하고 있지만,
     *   사용자의 이메일, 이름 등의 상세한 정보는 포함하고 있지 않다.
     * - loadUser 메서드에서는 이 OAuth2UserRequest를 사용하여 카카오 인증 서버로 사용자 정보를 요청하고,
     *   그 결과를 바탕으로 로컬 데이터베이스에서 사용자를 인증하고 사용자의 세부 정보를 로드하는 작업을 수행하여
     *   시큐리티 인증을 위한 객체로 사용함.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest....{}", userRequest);

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("clientName {} ",  clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest); // DB에 저장된 사용자 정보 조회

        Map<String, Object> paramMap = oAuth2User.getAttributes();

        String email = null;

        switch (clientName) {
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        log.info("===============================");
        log.info("카카오에서 받아온 이메일 : " + email);
        log.info("===============================");

        return generateDTO(email, paramMap);
    }

    private OAuth2User  generateDTO(String email, Map<String, Object> params) {

        MemberVo result = loginMapper.login(email); // DB에 저장된 사용자 정보 조회

        if (result == null) {   // DB에 저장된 사용자 정보가 없으면
            // 새로운 사용자 생성

            // 사용자 권한 설정(하드코딩)
            List<String> roles = Arrays.asList("ROLE_USER");

            MemberVo member = new MemberVo(
                    email,
                    passwordEncoder.encode("1111"), // 비밀번호 설정(하드코딩)
                    null,
                    email,
                    0,
                    roles,
                    false,
                    true,
                    params
            );

            // 사용자 저장
            loginMapper.save(member);

            // 권한 저장
            loginMapper.saveRole(member.getMemberId(), "ROLE_USER");

            return new SocialMemberDto(
                    member.getMemberId(),
                    member.getPassword(),
                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()),
                    params
            );
        } else {
            List<SimpleGrantedAuthority> authorities = result.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return new SocialMemberDto(
                    result.getMemberId(),
                    result.getPassword(),
                    authorities,
                    params
            );

        }
    }

    private String getKakaoEmail(Map<String, Object> paramMap) {
        log.info("KAKAO-----------------------------------------");
        Object value = paramMap.get("kakao_account");
        log.info(value);
        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String) accountMap.get("email");
        log.info("email..." + email);
        return email;
    }
}
