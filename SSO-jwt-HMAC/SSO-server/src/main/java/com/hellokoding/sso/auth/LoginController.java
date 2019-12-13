package com.hellokoding.sso.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XieShaoping
 */
@Controller
public class LoginController {
    private static final String jwtTokenCookieName = "JWT-TOKEN";
    private static final String signingKey = "signingKey";
    private static final Map<String, String> credentials = new HashMap<>();

    public LoginController() {
        credentials.put("hellokoding", "hellokoding");
        credentials.put("hellosso", "hellosso");
        credentials.put("xieshaoping", "xieshaoping");
    }

    @RequestMapping("/")
    public String home(){
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(HttpServletResponse httpServletResponse, String username, String password, String redirect, Model model){
        if (username == null || !credentials.containsKey(username) || !credentials.get(username).equals(password)){
            model.addAttribute("error", "无效的用户名或密码！");
            return "login";
        }
        //允许访问cookie的域名
        String domain="yanxiaoping.top";
        //生成token
        String token = JwtUtil.generateToken(signingKey, username);
        httpServletResponse.setHeader("jwt-header",token);
        httpServletResponse.setHeader("Authorization","Bearer "+token);
        CookieUtil.create(httpServletResponse, jwtTokenCookieName, token, false, -1, domain);
        return "redirect:" + redirect;
    }
}
