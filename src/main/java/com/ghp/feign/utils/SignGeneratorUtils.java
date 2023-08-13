package com.ghp.feign.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * @author ghp
 * @title 签名生成器工具类
 * @description
 */
public class SignGeneratorUtils {
    public static String generateSign(String body, String secretKey) {
        // 采用SHA加密算法对secretKey进行加密，作为签名，body相当于盐
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        String content = body + secretKey;
        return digester.digestHex(content);
    }
}
