package com.hellokoding.sso.resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author XieShaoping
 */
@Controller
public class ResourceController {
    private static final String jwtTokenCookieName = "JWT-TOKEN";

    @RequestMapping("/")
    public String home() {
        return "redirect:/protected-resource";
    }

    @RequestMapping("/protected-resource")
    public String protectedResource() {
        return "protected-resource";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        JwtUtil.invalidateRelatedTokens(httpServletRequest);
        CookieUtil.clear(httpServletResponse, jwtTokenCookieName);
        return "redirect:/";
    }
}
