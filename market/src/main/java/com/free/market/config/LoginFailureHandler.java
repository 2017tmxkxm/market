package com.free.market.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String loginFailMsg = "존재하지 않는 사용자입니다.";

        if (exception instanceof UsernameNotFoundException || exception instanceof InternalAuthenticationServiceException) {
            loginFailMsg = "존재하지 않는 사용자입니다.";
        } else if (exception instanceof BadCredentialsException) {
            loginFailMsg = "아이디 또는 비밀번호가 틀렸습니다..";
        }

        loginFailMsg = URLEncoder.encode(loginFailMsg, "UTF-8");
        setDefaultFailureUrl("/member/login?error=true&exception="+loginFailMsg);

        super.onAuthenticationFailure(request, response, exception);
    }
}
