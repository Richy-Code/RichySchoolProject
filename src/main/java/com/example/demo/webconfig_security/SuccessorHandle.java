package com.example.demo.webconfig_security;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SuccessorHandle extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
       String targetUrl = determineUrl(authentication);

       if(response.isCommitted()){
           return;
       }
       RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
       redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineUrl(Authentication authentication){
        String url = "";
        List<String> list = new ArrayList<>();
        for(GrantedAuthority authority : authentication.getAuthorities()){
            list.add(authority.getAuthority());
        }
        if(list.contains("admin")){
            url = "/admin";
        }
        if(list.contains("user")){
            url = "/teacher_home_page";
        }
        return url;
    }
}
