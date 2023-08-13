package com.ghp.feign.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.ghp.feign.utils.SignGeneratorUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ghp
 * @title API调用客户端
 * @description
 */
public class ApiClient {

    public static String DEFAULT_GATEWAY_HOST = "http://127.0.0.1:8090";

    private String accessKey;
    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * 调用接口，使用内部默认的网关主机名
     */
    public String invokeInterface(String params, String url, String method){
        return invokeInterface(params, url, method, DEFAULT_GATEWAY_HOST);
    }

    /**
     * 调用接口，自定义网关主机名
     */
    public String invokeInterface(String params, String url, String method, String gatewayHost){
        HttpResponse httpResponse = HttpRequest.post(gatewayHost + url)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(params, method))
                .body(params)
                .execute();
        return JSONUtil.formatJsonStr(httpResponse.body());
    }

    private Map<String, String> getHeaderMap(String body, String method) {
        HashMap<String, String> map = new HashMap<>();
        // 参数1：公钥
        map.put("accessKey", accessKey);
        // 参数2：随机数，防止重放攻击
        map.put("nonce", RandomUtil.randomNumbers(10));
        // 参数3：时间期限，该段时间内随机数只能出现一次（能够减轻服务器的压力，不然永久记录随机数太浪费资源了）
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        // 参数4：设置签名
        // 先进行URL编码，防止一些特殊字符造成的歧义
        body = URLUtil.encode(body, CharsetUtil.CHARSET_UTF_8);
        map.put("sign", SignGeneratorUtils.generateSign(body, secretKey));
        // 参数5：请求体（可要可不要）
        map.put("body", body);
        // 参数6：请求方式（可要可不要）
        map.put("method", method);
        return map;
    }

}
