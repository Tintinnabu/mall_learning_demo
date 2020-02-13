package com.taoshi.springboot_mybatis.controller;

import com.taoshi.springboot_mybatis.common.api.CommonResult;
import com.taoshi.springboot_mybatis.service.UmsMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Tintinnabu
 * @description
 * @data 2020/2/13
 */
@Controller
@Api(tags = "UmsMemberController",description = "会员登录注册管理")
@RequestMapping("/sso")
public class UmsMemberController {

    @Autowired
    private UmsMemberService umsMemberService;

    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getAuthCode",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getAuthCode(@RequestParam("telephone") String telephone){
        return umsMemberService.generateAuthCode(telephone);
    }

    @ApiOperation("校验验证码")
    @RequestMapping(value = "verifyAuthCode",method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updatePassword(@RequestParam("telephone") String telephone,
                                       @RequestParam("authCode") String authCode){
        return umsMemberService.verifyAuthCode(telephone,authCode);
    }
}
