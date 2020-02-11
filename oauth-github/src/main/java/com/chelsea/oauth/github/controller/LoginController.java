package com.chelsea.oauth.github.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录controller
 * @author shevchenko
 *
 */
@Controller
public class LoginController {
    
    /**
     * 跳转到登录页面
     * @return
     */
    @RequestMapping("/")
    public String login() {
        return "login";
    }

}
