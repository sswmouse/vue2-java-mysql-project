package com.example.demo.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 前端静态资源控制器
 * 处理前端SPA应用的路由
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
@RestController
public class FrontendController {

    private static final String FRONTEND_DIST_PATH = new java.io.File("../frontend/dist/").getAbsolutePath();

    /**
     * 处理根路径
     */
    @GetMapping(value = {"/", "/index.html"})
    public ResponseEntity<String> handleRoot() {
        return serveIndexHtml();
    }


    /**
     * 返回index.html
     */
    private ResponseEntity<String> serveIndexHtml() {
        try {
            Resource resource = new FileSystemResource(FRONTEND_DIST_PATH + "/index.html");
            if (resource.exists() && resource.isReadable()) {
                String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(content);
            }
        } catch (IOException e) {
            // ignore
        }
        return ResponseEntity.notFound().build();
    }
}
