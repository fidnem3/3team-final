package com.javalab.board.security;

import com.javalab.board.dto.SocialMemberDto;
import com.javalab.board.repository.LoginMapper;
import com.javalab.board.vo.JobSeekerVo;
import com.javalab.board.vo.CompanyVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final LoginMapper loginMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            log.info("Processing userRequest: {}", userRequest);

            ClientRegistration clientRegistration = userRequest.getClientRegistration();
            String clientName = clientRegistration.getClientName();

            if (!"GitHub".equalsIgnoreCase(clientName) && !"Kakao".equalsIgnoreCase(clientName)) {
                throw new OAuth2AuthenticationException(new OAuth2Error("unsupported_client", "Unsupported client: " + clientName, null));
            }

            log.info("ClientName: {}", clientName);

            OAuth2User oAuth2User = super.loadUser(userRequest);
            Map<String, Object> attributes = oAuth2User.getAttributes();
            log.info("OAuth2User attributes: {}", attributes);

            String userId;
            String email;

            if ("GitHub".equals(clientName)) {
                userId = String.valueOf(attributes.get("id"));
                email = (String) attributes.get("email");
                log.info("GitHub userId: {}, email: {}", userId, email);
            } else { // Kakao
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                userId = String.valueOf(attributes.get("id"));
                email = kakaoAccount != null ? (String) kakaoAccount.get("email") : null;

                log.info("Kakao attributes: {}", attributes);
                log.info("Kakao account: {}", kakaoAccount);
                log.info("Kakao userId: {}, email: {}", userId, email);
            }

            log.info("Received ID: {}", userId);

            return processUser(userId, email, attributes);
        } catch (OAuth2AuthenticationException e) {
            log.error("OAuth2 Authentication Exception: ", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error in loadUser: ", e);
            throw new OAuth2AuthenticationException(new OAuth2Error("authentication_error", "Authentication failed: " + e.getMessage(), null));
        }
    }

    private OAuth2User processUser(String userId, String email, Map<String, Object> attributes) {
        JobSeekerVo jobSeeker = loginMapper.loginJobSeeker(userId);
        CompanyVo company = loginMapper.loginCompany(userId);

        if (jobSeeker != null) {
            // JobSeeker 처리
            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            log.info("Authorities for user {}: {}", userId, authorities);

            return new SocialMemberDto(
                    userId,
                    jobSeeker.getName(),
                    email != null ? email : "noemail@example.com",
                    authorities,
                    attributes,
                    true
            );
        } else if (company != null) {
            // Company 처리
            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            log.info("Authorities for user {}: {}", userId, authorities);

            return new SocialMemberDto(
                    userId,
                    company.getCompanyName(),
                    email != null ? email : "noemail@example.com",
                    authorities,
                    attributes,
                    true
            );
        } else {
            // 새로운 사용자 처리
            String password = passwordEncoder.encode("1111");
            Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

            JobSeekerVo newJobSeeker = new JobSeekerVo();
            newJobSeeker.setJobSeekerId(userId);
            newJobSeeker.setPassword(password);
            newJobSeeker.setName("NoName");
            newJobSeeker.setEmail(email != null ? email : "noemail@example.com");
            newJobSeeker.setAddress("NoAddress");
            loginMapper.saveJobSeeker(newJobSeeker);
            loginMapper.saveRole(userId, "USER");

            log.info("New JobSeeker created: {}", newJobSeeker);

            return new SocialMemberDto(
                    userId,
                    "NoName",
                    email != null ? email : "noemail@example.com",
                    authorities,
                    attributes,
                    true
            );
        }
    }
}
