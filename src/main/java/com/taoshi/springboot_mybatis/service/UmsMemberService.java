package com.taoshi.springboot_mybatis.service;

import com.taoshi.springboot_mybatis.common.api.CommonResult;

/**
 * @author Tintinnabu
 * @description
 * @data 2020/2/13
 */
public interface UmsMemberService {

    /**
     * 生成验证码
     * @param telephone
     * @return
     */
    CommonResult generateAuthCode(String telephone);

    /**
     * 校验验证码
     * @param telephone
     * @param authCode
     * @return
     */
    CommonResult verifyAuthCode(String telephone,String authCode);
}
