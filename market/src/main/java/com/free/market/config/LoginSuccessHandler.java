package com.free.market.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    // RequestCache - 인터페이스 HttpSessionRequestCache - 구현체
    // 클라이언트가 접근하고자 했던 자원의 주소 정보, 헤더값, 쿠키값들을 담고 있는 request 객체를 HttpSessionRequestCache가 저장하고 있다.
    private final RequestCache requestCache = new HttpSessionRequestCache();

    // FilterChain chain 제거 후 Handler 적용 가능하게 되었음.
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("LoginSuccessHandler.onAuthenticationSuccess");
        // Security가 요청을 가로챈 경우 사용자가 원래 요청했던 URI 정보를 저장한 객체
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // 정보가 있을 경우 가져와서 사용
        if(savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();

            // 세션에 저장된 객체를 다 사용한 뒤에는 메모리 누수 방지를 위해 삭제
            requestCache.removeRequest(request, response);

            response.sendRedirect(redirectUrl);
        } else {
            System.out.println("LoginSuccessHandler.onAuthenticationSuccess.else");
            response.sendRedirect("/item");
        }
    }

    // 로그인 실패 후 성공시 남아있는 에러 세션 제거
    protected void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
