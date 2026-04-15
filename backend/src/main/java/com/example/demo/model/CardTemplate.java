package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 卡片模板实体类
 */
@Entity
@Table(name = "card_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", nullable = false, length = 50)
    private String templateName;

    @Column(name = "template_code", nullable = false, length = 20, unique = true)
    private String templateCode;

    @Column(name = "template_style", columnDefinition = "TEXT")
    private String templateStyle;

    @Column(name = "preview_image")
    private String previewImage;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
