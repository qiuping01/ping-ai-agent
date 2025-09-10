package com.ping.aiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ping.aiagent.contant.TestAPI;

public class VolcengineImageChat {

    public static void main(String[] args) {
        String url = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";
        String token = TestAPI.VolcengineImageChat_Api_KET;

        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("API密钥不能为空");
        }

        String imageUrl = "https://cdn.jsdmirror.com/gh/qiuping01/my-images/202508191817326.jpg";
        try {
            int statusCode = HttpRequest.get(imageUrl)
                    .timeout(10000)
                    .execute()
                    .getStatus();
            if (statusCode != 200) {
                System.err.println("警告: 图片URL可能无法访问，状态码: " + statusCode);
            }
        } catch (Exception e) {
            System.err.println("警告: 无法验证图片URL: " + e.getMessage());
        }

        JSONObject requestBody = new JSONObject();
        requestBody.set("model", "doubao-seed-1-6-250615");

        JSONArray messages = new JSONArray();
        JSONArray contentArray = new JSONArray();

        JSONObject imageObject = new JSONObject();
        imageObject.set("type", "image_url");
        JSONObject imageUrlObject = new JSONObject();
        imageUrlObject.set("url", imageUrl);
        imageObject.set("image_url", imageUrlObject);
        contentArray.add(imageObject);

        JSONObject textObject = new JSONObject();
        textObject.set("type", "text");
        textObject.set("text", "图片主要讲了什么?");
        contentArray.add(textObject);

        JSONObject userMessage = new JSONObject();
        userMessage.set("role", "user");
        userMessage.set("content", contentArray);

        messages.add(userMessage);
        requestBody.set("messages", messages);

        int maxRetries = 3;
        int retryCount = 0;
        String response = null;

        while (retryCount < maxRetries) {
            try {
                System.out.println("发送请求到: " + url);
                long startMs = System.currentTimeMillis();

                response = HttpRequest.post(url)
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + token)
                        .body(requestBody.toString())
                        .timeout(120000)
                        .execute()
                        .body();

                long costMs = System.currentTimeMillis() - startMs;

                // 直观输出
                printPrettyResponse(response, imageUrl, "图片主要讲了什么?", costMs);

                break;
            } catch (Exception e) {
                retryCount++;
                System.err.println("请求失败，第 " + retryCount + " 次重试...");
                System.err.println("错误信息: " + e.getMessage());

                if (retryCount >= maxRetries) {
                    System.err.println("已达到最大重试次数，放弃请求");
                    throw new RuntimeException("请求失败，已达到最大重试次数", e);
                }
                try {
                    Thread.sleep(2000L * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试等待被中断", ie);
                }
            }
        }
    }

    private static void printPrettyResponse(String response, String imageUrl, String userQuestion, long costMs) {
        if (response == null || response.isEmpty()) {
            System.out.println("未收到响应");
            return;
        }

        JSONObject resp;
        try {
            resp = JSONUtil.parseObj(response);
        } catch (Exception e) {
            System.out.println("收到非JSON响应，原始内容：\n" + response);
            return;
        }

        // 错误处理
        if (resp.containsKey("error")) {
            JSONObject error = resp.getJSONObject("error");
            System.out.println("\n================== 豆包 AI 响应（错误） ==================");
            System.out.println("错误代码: " + error.getStr("code", "-"));
            System.out.println("错误信息: " + error.getStr("message", "-"));
            System.out.println("原始响应(JSON)：\n" + JSONUtil.toJsonPrettyStr(resp));
            System.out.println("========================================================");
            return;
        }

        String id = resp.getStr("id", "-");
        String model = resp.getStr("model", "-");
        JSONObject usage = resp.getJSONObject("usage");
        Integer promptTokens = usage.getInt("prompt_tokens", null);
        Integer completionTokens = usage.getInt("completion_tokens", null);
        Integer totalTokens = usage.getInt("total_tokens", null);

        // 提取助手回答（兼容字符串或分段数组）
        String assistantAnswer = extractAssistantAnswer(resp);

        System.out.println("\n================== 豆包 AI 响应（已格式化） ===============");
        System.out.println("请求信息");
        System.out.println("- 图片URL    : " + imageUrl);
        System.out.println("- 用户问题    : " + userQuestion);
        System.out.println("- 耗时        : " + costMs + " ms");

        System.out.println("\n元信息");
        System.out.println("- 请求ID      : " + id);
        System.out.println("- 模型        : " + model);
        if (promptTokens != null || completionTokens != null || totalTokens != null) {
            System.out.println("- Token用量   : prompt=" + safeInt(promptTokens)
                    + ", completion=" + safeInt(completionTokens)
                    + ", total=" + safeInt(totalTokens));
        }

        System.out.println("\n助手回答");
        System.out.println(assistantAnswer == null || assistantAnswer.isEmpty() ? "(空)" : assistantAnswer);

        System.out.println("\n原始响应(JSON)"); // 便于排查
        System.out.println(JSONUtil.toJsonPrettyStr(resp));
        System.out.println("========================================================\n");
    }

    private static String extractAssistantAnswer(JSONObject resp) {
        try {
            JSONArray choices = resp.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) {
                return "";
            }
            JSONObject first = choices.getJSONObject(0);
            if (first == null) return "";
            JSONObject message = first.getJSONObject("message");
            if (message == null) return "";

            Object content = message.get("content");
            if (content == null) return "";

            // 可能是字符串
            if (content instanceof CharSequence) {
                return String.valueOf(content).trim();
            }

            // 也可能是数组（多模态分段）
            if (content instanceof JSONArray) {
                JSONArray parts = (JSONArray) content;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < parts.size(); i++) {
                    JSONObject part = parts.getJSONObject(i);
                    String type = part.getStr("type", "");
                    if ("text".equalsIgnoreCase(type)) {
                        builder.append(part.getStr("text", ""));
                    } else if ("image_url".equalsIgnoreCase(type)) {
                        JSONObject img = part.getJSONObject("image_url");
                        String url = img == null ? "" : img.getStr("url", "");
                        builder.append("\n[图片] ").append(url);
                    }
                }
                return builder.toString().trim();
            }
            return String.valueOf(content).trim();
        } catch (Exception e) {
            return "";
        }
    }

    private static String safeInt(Integer v) {
        return v == null ? "-" : String.valueOf(v);
    }
}