package com.javalab.board.config;

import com.javalab.board.handler.AuthFailureHandler;
import com.javalab.board.handler.AuthSuccessHandler;
import com.javalab.board.security.CustomOAuth2UserService;
import com.javalab.board.security.dto.CustomUserDetails;
import com.javalab.board.security.handler.CustomAccessDeniedHandler;
import com.javalab.board.security.handler.CustomSocialLoginSuccessHandler;
import com.javalab.board.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private AuthFailureHandler authFailureHandler;

	@Autowired
	private AuthSuccessHandler authSuccessHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new CustomSocialLoginSuccessHandler(passwordEncoder());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService) throws Exception {

		http.userDetailsService(userDetailsService);

		http
				.headers(headers -> headers
						.frameOptions(frameOptions -> frameOptions.sameOrigin()) // 람다 기반 DSL 사용
				)
				.csrf(csrf -> csrf
						.ignoringRequestMatchers(new AntPathRequestMatcher("/jobPost/uploadImage"))
				)
				.formLogin(formLogin -> formLogin
						.loginPage("/member/login")
						.loginProcessingUrl("/member/action")
						.successHandler(authSuccessHandler)
						.failureHandler(authFailureHandler)
				)
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/index")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
				)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/scss/**", "/lib/**", "/assets/**", "/static/**").permitAll()
						.requestMatchers("/", "/home", "/about", "/contact", "/index", "/jobPost/jobPostList", "/jobPost/detail/**").permitAll()
						.requestMatchers("/board/**", "/upload/**", "/jobPost/logo/**", "/jobPost/uploaded/**").permitAll()
						.requestMatchers("/member/**", "/member/adminJoin", "/api/**").permitAll()
						.requestMatchers("/board/**", "/admin/admin").permitAll()
						.requestMatchers("/admin/adminPage").hasRole("ADMIN")
						.requestMatchers("/admin/blacklist").hasRole("ADMIN")


						.requestMatchers("/board/**", "/upload/**", "/jobPost/logo/**").permitAll()
						.requestMatchers("/error").permitAll()  // /error 경로 접근 허용
						.requestMatchers("/admin/suggestionList").hasRole("ADMIN")
						.requestMatchers("/member/adminPage").hasRole("ADMIN")
						.requestMatchers("/member/companyPage").hasRole("COMPANY")
						.requestMatchers("/member/jobSeekerPage").hasRole("USER")
						.anyRequest().authenticated()
				)
				.sessionManagement(sessionManagement -> sessionManagement
						.maximumSessions(1)
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
		return http.build();
	}
}
