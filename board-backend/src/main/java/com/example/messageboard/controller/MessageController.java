package com.example.messageboard.controller;

import com.example.messageboard.entity.Message;
import com.example.messageboard.entity.User;
import com.example.messageboard.repository.MessageRepository;
import com.example.messageboard.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 留言板控制器
 * 处理所有与留言相关的请求，包括留言的增删改查、用户登录等功能
 */
@RestController
@RequestMapping("/api")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * 添加新留言
     * @param message 留言内容对象
     * @param request HTTP请求对象，用于获取用户IP
     * @return 保存后的留言对象
     */
    @PostMapping("/messages")
    public Message addMessage(@RequestBody Message message, HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        message.setIp(ip);
        message.setTime(LocalDateTime.now());
        return messageRepository.save(message);
    }

    /**
     * 获取所有可见的留言
     * @return 留言列表，按时间倒序排列
     */
    @GetMapping("/messages")
    public List<Message> getMessages() {
        return messageRepository.findByVisibleTrueOrderByTimeDesc();
    }

    /**
     * 用户登录
     * @param request 登录请求，包含用户名和密码
     * @return 登录结果，包含成功标志和用户角色
     */
    @PostMapping("/messages/login")
    public Object login(@RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.username());
        if (user != null && user.getPassword().equals(request.password())) {
            return Map.of("success", true, "role", user.getRole());
        }
        return Map.of("success", false);
    }

    /**
     * 隐藏指定留言（管理员功能）
     * @param id 留言ID
     * @param adminId 执行隐藏操作的管理员ID
     */
    @PutMapping("/messages/{id}/hide")
    public void hideMessage(@PathVariable Long id, @RequestParam String adminId) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("留言不存在"));
        message.setVisible(false);
        message.setAdminId(adminId);
        messageRepository.save(message);
    }

    /**
     * 登录请求数据类
     */
    private record LoginRequest(
        @NotBlank(message = "用户名不能为空")
        String username,
        @NotBlank(message = "密码不能为空")
        String password
    ) {}
}
