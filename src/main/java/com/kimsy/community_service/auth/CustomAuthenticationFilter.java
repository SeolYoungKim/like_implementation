package com.kimsy.community_service.auth;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private static final String HTTP_HEADER_AUTHENTICATION = "Authentication";
    private static final String BLANK = "";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
        final String token = request.getHeader(HTTP_HEADER_AUTHENTICATION);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final AuthenticationToken authenticationToken = new AuthenticationToken(token);
        registerAuthenticationToSecurityContextHolder(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private static void registerAuthenticationToSecurityContextHolder(
            final AuthenticationToken authenticationToken) {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationToken.getAccountId(), BLANK,
                AuthorityUtils.createAuthorityList(authenticationToken.getAccountType()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
