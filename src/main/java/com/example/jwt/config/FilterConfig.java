package com.example.jwt.config;

import com.example.jwt.filter.MyFilter1;
import com.example.jwt.filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1() {
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*"); // 모든 Url 요청에 대해 MyFilter1 을 사용함
        bean.setOrder(0); // 낮은 번호가 우선 순위가 높음. 낮은 필터가 가장 먼저 실행됨
        return bean;
    }
    // SecurityConfig 에서 아래와 같이 필터를 체인에 걸 수 있지만
    // http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
    // 위처럼 필터를 설정할 수도 있다. 다만, 필터체인이 기본적으로 먼저 동작하고, addFilterBefor() 을 통해
    // 지정하는 필터 이전에 실행되도록 할 수 있다.

    @Bean
    public FilterRegistrationBean<MyFilter2> filter2() {
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*"); // 모든 Url 요청에 대해 MyFilter1 을 사용함
        bean.setOrder(1);
        return bean;
    }
}
