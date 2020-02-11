package com.chelsea.oauth.github.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.chelsea.oauth.github.domain.AccessTokenVO;
import com.chelsea.oauth.github.domain.UserInfoVo;

/**
 * 认证controller
 * 
 * @author shevchenko
 *
 */
@Controller
@RequestMapping("/oauth")
public class OAuthController {
    
    /**
     * 接收授权码
     * 
     * @param code
     * @return
     */
    @RequestMapping("/redirect")
    public String redirect(ModelMap map, String code) {
        String client_id = "51171f9864c36ebfbf33";
        String client_secret = "8333262d9b19fe30338ef503620a5e186f4e30cf";
        String url = "https://github.com/login/oauth/access_token";
        HttpHeaders headers = new HttpHeaders();
        headers.add("accept", "application/json");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", client_id);
        params.add("client_secret", client_secret);
        params.add("code", code);
        // 发送post请求令牌
        AccessTokenVO accessTokenVO = (AccessTokenVO)sendPostRequest(url, headers, params, AccessTokenVO.class).getBody();
        String access_token = accessTokenVO.getAccess_token();
        // 发送get请求用户数据
        url = "https://api.github.com/user";
        headers = new HttpHeaders();
        headers.add("accept", "application/json");
        headers.add("Authorization", "token " + access_token);
        params = new LinkedMultiValueMap<>();
        UserInfoVo userInfoVo = (UserInfoVo)sendGetRequest(url, headers, params, UserInfoVo.class).getBody(); //bbb35c1c02c8cd4181a353b1c18b92763bf486aa
        map.addAttribute("name", userInfoVo.getName());
        return "home";
    }
    
    /**
     * 发送post请求
     * 
     * @param url
     * @param headers
     * @param params
     * @param classes
     * @return
     */
    public static ResponseEntity<?> sendPostRequest(String url, HttpHeaders headers, MultiValueMap<String, String> params, Class<?> classes){
        RestTemplate client = new RestTemplate();
        HttpMethod method = HttpMethod.POST;
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<?> response = client.exchange(url, method, requestEntity, classes);
        return response;
    }
    
    /**
     * 发送get请求
     * 
     * @param url
     * @param headers
     * @param params
     * @param classes
     * @return
     */
    public static ResponseEntity<?> sendGetRequest(String url, HttpHeaders headers, MultiValueMap<String, String> params, Class<?> classes){
        RestTemplate client = new RestTemplate();
        HttpMethod method = HttpMethod.GET;
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<?> response = client.exchange(url, method, requestEntity, classes);
        return response;
    }

}
