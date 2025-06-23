package com.example.messageboard.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean active = true;
    
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();
    
    public enum UserRole {
        USER,      // 普通用户
        MODERATOR, // 版主
        ADMIN      // 管理员
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        lastLogin = LocalDateTime.now();
    }
}
