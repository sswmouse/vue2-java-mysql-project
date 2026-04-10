package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {

    // 上传文件保存目录
    private static final String UPLOAD_DIR = "uploads/";

    /**
     * 文件上传接口
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                result.put("code", 400);
                result.put("message", "请选择要上传的文件");
                return ResponseEntity.badRequest().body(result);
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null ||
                (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg"))) {
                result.put("code", 400);
                result.put("message", "只支持 JPG/PNG 格式的图片");
                return ResponseEntity.badRequest().body(result);
            }

            // 检查文件大小 (2MB)
            if (file.getSize() > 2 * 1024 * 1024) {
                result.put("code", 400);
                result.put("message", "图片大小不能超过 2MB");
                return ResponseEntity.badRequest().body(result);
            }

            // 创建上传目录
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + fileExtension;

            // 保存文件
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            Files.copy(file.getInputStream(), filePath);

            // 构建返回结果
            Map<String, String> data = new HashMap<>();
            data.put("url", "/" + UPLOAD_DIR + filename);
            data.put("filename", filename);

            result.put("code", 200);
            result.put("message", "上传成功");
            result.put("data", data);

            return ResponseEntity.ok(result);

        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 500);
            result.put("message", "上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("File upload service is running");
    }
}