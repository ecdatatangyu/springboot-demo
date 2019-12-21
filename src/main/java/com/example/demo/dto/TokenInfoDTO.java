package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author ty
 * @description
 * @date 2019/11/20 14:47
 */
@Data
@Builder(toBuilder=true)
@Accessors(chain = true)
public class TokenInfoDTO implements Serializable {
    private static final long serialVersionUID = -1490437070615186742L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String value;

    /**
     * 授权类型
     */
    private String grantType;

}
