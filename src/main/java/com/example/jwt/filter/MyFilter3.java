package com.example.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 : cos <- 만들어야 함. 정상적인 id, pw 로 로그인이 완료 되면 토큰을 만들어 응답
        // 요청마다 header.Authorization의 value 값으로 토큰을 가지고 있음
        // 이때 토큰을 받으면 이 토큰이 서버가 만든 토큰이 맞는지 검증하면 됨. (RSA, HS256)
        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);

            System.out.println("필터 3");

            if (headerAuth.equals("cos")) { // Authorization 인증
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
            }
        }
    }
}
