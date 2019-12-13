package com.hellokoding.sso.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * @author XieShaoping
 */
public class JwtFilter extends OncePerRequestFilter {
    private static final String jwtTokenCookieName = "JWT-TOKEN";

    @Value("${privateKeyStrq}")
    private String privateKeyStr;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        privateKeyStr="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQP2LY/DO8arTxS8IaUgaYYX0q\n" +
                "  QVy4dFPljtFBIqiy0g74GTD5UnDAsoTEIdbKonxqDFnn4e0zx5tVP5Ge1Gw45l2V\n" +
                "  ixBpnwMR9Q0xAte5Aoc2W/SQ54OnuPSXGekydhJC8Kb/Mn3zLOLBP34Z/1N9FTn6\n" +
                "  fz+OfrFjaAG9KWACgwIDAQAB";
        try {
            username = JwtUtil.parseToken(httpServletRequest, jwtTokenCookieName, privateKeyStr);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (username != null) {
            httpServletRequest.setAttribute("username", username);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
