package com.example.messageboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String nickname;  // 匿名用户昵称

    @Column(nullable = false)
    private String ip;        // 留言者IP

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User author;      // 登录用户

    private LocalDateTime time;
    private boolean visible = true;
    private String adminId;   // 隐藏操作的管理员ID

    public boolean isAnonymous() {
        return author == null;
    }
}
