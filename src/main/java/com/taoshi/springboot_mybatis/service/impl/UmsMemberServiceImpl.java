package com.taoshi.springboot_mybatis.service.impl;

import com.taoshi.springboot_mybatis.common.api.CommonResult;
import com.taoshi.springboot_mybatis.service.RedisService;
import com.taoshi.springboot_mybatis.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @author Tintinnabu
 * @description
 * @data 2020/2/13
 * 生成验证码时，将自定义的Redis键值加上手机号生成一个Redis的key,
 * 以验证码为value存入到Redis中，并设置过期时间为自己配置的时间（这里为120s）。
 * 校验验证码时根据手机号码来获取Redis里面存储的验证码，并与传入的验证码进行比对。
 */
@Service
public class UmsMemberServiceImpl implements UmsMemberService {

    @Autowired
    private RedisService redisService;

    @Value("${redis.key.prefix.authCode}")
    private String REDIS_KEY_PREFIX_AUTH_CODE;
    @Value("${redis.key.expire.authCode}")
    private Long AUTH_CODE_EXPIRE_SECONDS;

    @Override
    public CommonResult generateAuthCode(String telephone) {
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        for(int i=0;i<6;i++){
            stringBuilder.append(random.nextInt(10));
        }

        redisService.set(REDIS_KEY_PREFIX_AUTH_CODE+telephone,stringBuilder.toString());
        redisService.expire(REDIS_KEY_PREFIX_AUTH_CODE+telephone,AUTH_CODE_EXPIRE_SECONDS);
        return CommonResult.success(stringBuilder.toString());
    }

    @Override
    public CommonResult verifyAuthCode(String telephone, String authCode) {
        if (StringUtils.isEmpty(authCode)){
            return CommonResult.failed("请输入验证码");
        }
        String realAuthCode=redisService.get(REDIS_KEY_PREFIX_AUTH_CODE+telephone);
        boolean result=authCode.equals(realAuthCode);
        if (result){
            return CommonResult.success(null,"验证码校验成功");
        }
        return CommonResult.failed("验证码校验失败");
    }
}
