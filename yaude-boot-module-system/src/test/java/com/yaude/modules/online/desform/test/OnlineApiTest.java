package com.yaude.modules.online.desform.test;

import com.alibaba.fastjson.JSONObject;
import com.yaude.JeecgSystemApplication;
import com.yaude.common.constant.CommonConstant;
import com.yaude.common.system.util.JwtUtil;
import com.yaude.common.util.RedisUtil;
import com.yaude.common.util.RestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * online api online表單單元測試
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = JeecgSystemApplication.class)
@SuppressWarnings({"FieldCanBeLocal", "SpringJavaAutowiredMembersInspection"})
public class OnlineApiTest {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 測試地址：實際使用時替換成你自己的地址
     */
    private final String BASE_URL = "http://localhost:8080/jeecg-boot/online/cgform/api/";

    // 請實際使用時替換成你自己的用戶名和密碼
    private final String USERNAME = "admin";
    private final String PASSWORD = "123456";

    /**
     * online表單code，實際使用時可以替換成你要測試的表單code
     * （測試表test_demo）
     */
    private final String ONLINE_CODE = "d35109c3632c4952a19ecc094943dd71";

    /**
     * 測試用例：新增
     */
    @Test
    public void testAdd() {
        // 用戶Token
        String token = this.getToken();
        // 請求地址
        String url = BASE_URL + "form/" + ONLINE_CODE;
        // 請求 Header （用于傳遞Token）
        HttpHeaders headers = this.getHeaders(token);
        // 請求方式是 POST 代表提交新增數據
        HttpMethod method = HttpMethod.POST;

        System.out.println("請求地址：" + url);
        System.out.println("請求方式：" + method);
        System.out.println("請求Token：" + token);

        JSONObject params = new JSONObject();
        params.put("name", "張三");
        params.put("sex", "1");
        params.put("age",15);
        params.put("descc", "<p>富文本編輯</p>");

        System.out.println("請求參數：" + params.toJSONString());

        // 利用 RestUtil 請求該url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, params, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回結果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查詢失敗");
        }
    }


    /**
     * 測試用例：修改
     */
    @Test
    public void testEdit() {
        // dataId
        String dataId = "1331797913880883201";
        // 用戶Token
        String token = this.getToken();
        // 請求地址
        String url = BASE_URL + "form/" + ONLINE_CODE;
        // 請求 Header （用于傳遞Token）
        HttpHeaders headers = this.getHeaders(token);
        // 請求方式是 PUT 代表提交修改數據
        HttpMethod method = HttpMethod.PUT;

        System.out.println("請求地址：" + url);
        System.out.println("請求方式：" + method);
        System.out.println("請求Token：" + token);

        JSONObject params = new JSONObject();
        params.put("id", dataId);
        params.put("name", "張三");
        params.put("sex", "1");
        params.put("age",30);
        params.put("descc", "<p>富文本編輯，重新編輯</p>");

        System.out.println("請求參數：" + params.toJSONString());

        // 利用 RestUtil 請求該url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, params, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回結果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查詢失敗");
        }
    }


    /**
     * 測試用例：刪除
     */
    @Test
    public void testDelete() {
        // 數據id
        String dataId = "1331797913880883201";
        // 用戶Token
        String token = this.getToken();
        // 請求地址
        String url = BASE_URL + "form/" + ONLINE_CODE + "/" + dataId;
        // 請求 Header （用于傳遞Token）
        HttpHeaders headers = this.getHeaders(token);
        // 請求方式是 DELETE 代表刪除數據
        HttpMethod method = HttpMethod.DELETE;

        System.out.println("請求地址：" + url);
        System.out.println("請求方式：" + method);
        System.out.println("請求Token：" + token);

        // 利用 RestUtil 請求該url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回結果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查詢失敗");
        }
    }

    /**
     * 測試用例：查詢記錄
     */
    @Test
    public void testQuery() {
        // 用戶Token
        String token = this.getToken();
        // 請求地址
        String url = BASE_URL + "getData/"+ ONLINE_CODE ;
        // 請求 Header （用于傳遞Token）
        HttpHeaders headers = this.getHeaders(token);
        // 請求方式是 GET 代表獲取數據
        HttpMethod method = HttpMethod.GET;

        System.out.println("請求地址：" + url);
        System.out.println("請求方式：" + method);
        System.out.println("請求Token：" + token);

        // 利用 RestUtil 請求該url
        ResponseEntity<JSONObject> result = RestUtil.request(url, method, headers, null, null, JSONObject.class);
        if (result != null && result.getBody() != null) {
            System.out.println("返回結果：" + result.getBody().toJSONString());
        } else {
            System.out.println("查詢失敗");
        }
    }

    private String getToken() {
        String token = JwtUtil.sign(USERNAME, PASSWORD);
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, 60);
        return token;
    }

    private HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        String mediaType = MediaType.APPLICATION_JSON_UTF8_VALUE;
        headers.setContentType(MediaType.parseMediaType(mediaType));
        headers.set("Accept", mediaType);
        headers.set("X-Access-Token", token);
        return headers;
    }
}