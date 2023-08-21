package com.example.jwt.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// username, password와 함께 "/login"이 요청되면 스프링 시큐리티에서 아래 필터가 동작함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // "/login" 요청 시 로그인 시도를 위해서 실행됨.
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter.attemptAuthentication() 실행됨 - 로그인 시도 중");

        // 1. username, password

        // 2. 정상인지 확인, authenticationManager로 로그인 시도 -> principalDetails 호출 되고
        // loadUserByUsername() 실행됨

        // 3. principalDetails를 세션에 담음 - 권한 관리를 위함

        // 4. JWT 토큰을 만들어서 응답

        return super.attemptAuthentication(request, response);
    }
}
