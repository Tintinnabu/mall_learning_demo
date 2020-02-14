package com.taoshi.springboot_mybatis.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.swing.table.TableRowSorter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tintinnabu
 * @description 用于生成和解析JWT token的工具类
 * generateToken(UserDetails userDetails) :用于根据登录用户信息生成token
 * getUserNameFromToken(String token)：从token中获取登录用户的信息
 * validateToken(String token, UserDetails userDetails)：判断token是否还有效
 * @data 2020/2/14
 */

@Component
public class JwtTokenUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME="sub";
    private static final String CLAIM_KEY_CREATED="created";

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpireDate())
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpireDate() {
        return new Date(System.currentTimeMillis()+expiration*1000);
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token){
        Claims claims=null;
        try {
            claims=Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            LOGGER.info("JWT格式验证失败:{}",token);
        }
        return claims;
    }

    /**
     * getUserNameFromToken(String token)：从token中获取登录用户的信息
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token){
        String username;
        try {
            Claims claims=getClaimsFromToken(token);
            username=claims.getSubject();
        }catch (Exception e){
            username=null;
        }
        return username;
    }

    /**
     * 验证token
     * validateToken(String token, UserDetails userDetails)：判断token是否还有效
     * @param token
     * @param userDetails
     * @return
     */
    public boolean validateToken(String token, UserDetails userDetails){
        String username=getUserNameFromToken(token);
        if (username==null)
            return false;
        return username.equals(userDetails.getUsername())&&!isTokenExpired(token);
    }


    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate=generateExpireDate();
        return expiredDate.before(new Date());
    }

    /**
     * generateToken(UserDetails userDetails) :用于根据登录用户信息生成token
     * 根据用户信息生成token
     */
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims=new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME,userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }


    /**
     * 刷新token
     */
    public boolean canReflash(String token){
        return isTokenExpired(token);
    }


    /**
     * 刷新token
     */
    public String reflashToken(String token){
        Claims claims=getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED,new Date());
        return generateToken(claims);
    }

}
