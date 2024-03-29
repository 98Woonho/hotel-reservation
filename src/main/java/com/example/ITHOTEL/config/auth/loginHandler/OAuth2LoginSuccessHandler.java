package com.example.ITHOTEL.config.auth.loginHandler;

import com.example.ITHOTEL.config.auth.PrincipalDetails;
import com.example.ITHOTEL.config.auth.jwt.JwtProperties;
import com.example.ITHOTEL.config.auth.jwt.JwtTokenProvider;
import com.example.ITHOTEL.config.auth.jwt.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

            //--------------------------------------
            //JWT ADD
            //--------------------------------------
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            // 쿠키 생성
            Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, tokenInfo.getAccessToken());
            cookie.setMaxAge(JwtProperties.EXPIRATION_TIME); // 쿠키의 만료시간 설정
            cookie.setPath("/");
            response.addCookie(cookie);
            //--------------------------------------

            PrincipalDetails principalDetails =(PrincipalDetails) authentication.getPrincipal();
            Collection<? extends GrantedAuthority> collection =  authentication.getAuthorities();
            collection.forEach( (role)->{
                String role_str =  role.getAuthority();

                try {
                    if (role_str.equals("ROLE_USER") && principalDetails.getUserDto().getAddr1() == null)
                        response.sendRedirect("/user/Oauthjoin");
                    else
                        response.sendRedirect("/");
                }catch(Exception e){
                    e.printStackTrace();
                }
            });
        }
}
