package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.example.jwt.config.jwt.JwtProperties.*;

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

        try {
//            BufferedReader br = request.getReader();
//
//            String input = null;
//            while((input = br.readLine()) != null) {
//                System.out.println(input);
//            }
            ObjectMapper om = new ObjectMapper(); // 이 클래스가 Json 데이터를 파싱해준다.
            User user = om.readValue(request.getInputStream(), User.class);

            // 1. username, password
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 2. 정상인지 확인, authenticationManager로 로그인 시도
            // -> principalDetails 호출 되고 loadUserByUsername() 실행됨
            // 이후 정상이면 authentication이 리턴 됨

            return authenticationManager.authenticate(authenticationToken); // authentication 객체가 세션 영역에 저장됨.
            // JWT 토큰을 사용하면서 세션은 필요 없지만, 단지 권한 처리를 위해 만들어 주는 것임.
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // attemptAuthentication() 실행 후 인증이 정상적으로 되면 successfulAuthentication() 실행됨
    // JWT 토큰을 만들어서 request 요청 사용자에게 JWT 토큰을 응답해줌.
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) {

        System.out.println("JwtAuthenticationFilter.successfulAuthentication() 실행됨 - 인증 완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject("cos 토큰")
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료시간 (1시간)
                .withClaim("id", principalDetails.getUser().getId()) // 계정 id 정보
                .withClaim("username", principalDetails.getUser().getUsername()) // 계정 정보
                .sign(Algorithm.HMAC512(SECRET));

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + jwtToken);
    }
}
