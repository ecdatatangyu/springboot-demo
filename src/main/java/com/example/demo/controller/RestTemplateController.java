package com.example.demo.controller;

import com.example.demo.dto.TokenInfoDTO;
import com.example.demo.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

@RestController
@RequestMapping("/v1/huawei")
public class RestTemplateController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/getToken")
    public void getToken(@RequestParam(required = true) String userName,
                           @RequestParam(required = true) String password) throws Exception {
        String url =  "https://172.16.59.39:26335/rest/plat/smapp/v1/oauth/token";
        TokenInfoDTO tokenInfoDTO = TokenInfoDTO.builder().
                userName(userName).
                value(password).
                grantType("password").
                build();
        String postForEntity = RestTemplateUtil.postForEntity(url, tokenInfoDTO);
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }
}
