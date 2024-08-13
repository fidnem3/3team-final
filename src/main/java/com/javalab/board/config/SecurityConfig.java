package com.javalab.board.config;

import com.javalab.board.handler.AuthFailureHandler;
import com.javalab.board.handler.AuthSuccessHandler;
import com.javalab.board.security.CustomOAuth2UserService;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.security.handler.CustomAccessDeniedHandler;
import com.javalab.board.security.handler.CustomSocialLoginSuccessHandler;
import com.javalab.board.service.CompanyService;
import com.javalab.board.service.CustomUserDetailsService;
import com.javalab.board.service.JobSeekerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Autowired
    private  JobSeekerService jobSeekerService;

    @Autowired
    private  CompanyService companyService;

	@Autowired
	private  CustomUserDetailsService userDetailsService;

    private final AuthSuccessHandler authSuccessHandler;

	@Autowired
	private  AuthFailureHandler authFailureHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {  // 비밀번호 암호화를 위한 빈
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {  // 권한이 없는 페이지에 접근 시 처리를 담당하는 빈
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {  // 소셜 로그인 성공 후처리를 담당하는 빈
		return new CustomSocialLoginSuccessHandler(passwordEncoder());
	}

	/**
	 * [securityFilterChain 메소드]
	 * - HttpSecurity 빈 등록
	 * - HttpSecurity 객체를 이용하여 보안 설정
	 * @param http : HttpSecurity 객체
	 * @param customOAuth2UserService : CustomOAuth2UserService 객체
	 * @return SecurityFilterChain : SecurityFilterChain 객체
	 * @throws Exception : 예외처리
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {

		AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);

		auth.userDetailsService(username -> {
			UserDetails userDetails = jobSeekerService.loadUserByUsername(username);
			if (userDetails != null) {
				return userDetails;
			}
			userDetails = companyService.loadUserByUsername(username);
			if (userDetails != null) {
				return userDetails;
			}
			throw new UsernameNotFoundException("User not found with username: " + username);
		}).passwordEncoder(passwordEncoder());
		http.userDetailsService(userDetailsService);

		http
				.formLogin(formLogin -> formLogin
						.loginPage("/member/login")
						.loginProcessingUrl("/member/login")
						.successHandler(authSuccessHandler)
						.failureHandler(authFailureHandler)
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
						.logoutSuccessUrl("/member/login")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/scss/**", "/lib/**", "/assets/**").permitAll()
						.requestMatchers("/", "/home", "/about", "/contact", "/index", "/jobPost/jobPostList", "/jobPost/jobPostDetail").permitAll()  // 필요에 따라 추가
						.requestMatchers("/view/**").permitAll()
						.requestMatchers("/member/**").permitAll()
						.requestMatchers("/board/**").permitAll()
						.requestMatchers("/member/adminPage").hasRole("ADMIN")  // 관리자 페이지 접근 권한
						.requestMatchers("/member/companyPage").hasRole("COMPANY")  // 기업 전용 페이지 접근 권한
						.requestMatchers("/member/jobSeekerPage").hasRole("USER")  // 구직자 전용 페이지 접근 권한
						.anyRequest().authenticated() // 모든 요청에 대해 인증 필요
				)
				.sessionManagement(sessionManagement -> sessionManagement
						.maximumSessions(1) // 한 번에 하나의 세션만 허용
						.maxSessionsPreventsLogin(false)
						.expiredUrl("/login?error=true&exception=Have been attempted to login from a new place. or session expired")
				)
				.exceptionHandling(exceptionHandling -> exceptionHandling
						.accessDeniedHandler(accessDeniedHandler())
				)
				.oauth2Login(oauth2 -> oauth2
						.loginPage("/member/login")
						.successHandler(authenticationSuccessHandler())
						.userInfoEndpoint(userInfo -> userInfo
								.userService(customOAuth2UserService)
						)
				);

		http.authenticationManager(auth.build());  // 인증 매니저 설정

		return http.build();  // 설정한 HttpSecurity 객체 반환
	}
}
