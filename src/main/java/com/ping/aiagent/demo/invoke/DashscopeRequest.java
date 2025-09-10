package com.ping.aiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ping.aiagent.contant.TestAPI;

/**
 * HTTP 方式调用 AI
 */
public class DashscopeRequest {

    public static void main(String[] args) {
        // API密钥
        String apiKey = TestAPI.API_KEY;

        // 构建请求URL
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 构建请求JSON体
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", "qwen-plus");

        JSONObject input = new JSONObject();

        // 创建消息数组
        JSONArray messages = new JSONArray();

        // 添加系统消息
        messages.add(new JSONObject()
                .set("role", "system")
                .set("content", "You are a helpful assistant."));

        // 添加用户消息
        messages.add(new JSONObject()
                .set("role", "user")
                .set("content", "你是谁？"));

        input.set("messages", messages);

        JSONObject parameters = new JSONObject();
        parameters.set("result_format", "message");

        requestBody.set("input", input);
        requestBody.set("parameters", parameters);

        // 发送POST请求
        String response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .timeout(20000)
                .execute()
                .body();

        System.out.println(response);
    }
}