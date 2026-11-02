package com.example.taskapi.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

  @Value("${info.app.version:1.0.0}")
  private String appVersion;

  @GetMapping("/version")
  public Map<String, String> getVersion() {
    Map<String, String> response = new HashMap<>();
    response.put("version", appVersion);
    response.put("status", "running");
    return response;
  }
}
