package com.ping.aiagent.controller;

import com.ping.aiagent.common.BaseResponse;
import com.ping.aiagent.common.ResultUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("ok");
    }

}
