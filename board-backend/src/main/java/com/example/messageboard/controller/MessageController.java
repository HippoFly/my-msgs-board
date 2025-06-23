package com.example.messageboard.controller;

import com.example.messageboard.entity.Message;
import com.example.messageboard.entity.User;
import com.example.messageboard.repository.MessageRepository;
import com.example.messageboard.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * 留言板控制器
 * 处理所有与留言相关的请求，包括留言的增删改查、用户登录等功能
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
     * @return 操作结果
     */
    @PutMapping("/messages/{id}/hide")
    public ResponseEntity<?> hideMessage(@PathVariable Long id) {
        try {
            Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("留言不存在"));
            message.setVisible(false);
            messageRepository.save(message);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 显示指定留言（管理员功能）
     * @param id 留言ID
     * @return 操作结果
     */
    @PutMapping("/messages/{id}/show")
    public ResponseEntity<?> showMessage(@PathVariable Long id) {
        try {
            Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("留言不存在"));
            message.setVisible(true);
            messageRepository.save(message);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    /**
     * 获取所有留言（管理员功能）
     * @return 所有留言列表，按时间倒序排列
     */
    @GetMapping("/messages/all")
    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByTimeDesc();
    }

    /**
     * 获取所有用户信息（管理员功能）
     * @return 用户列表
     */
    @GetMapping("/messages/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            // 处理返回数据，不返回密码
            List<Map<String, Object>> userDTOs = users.stream()
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("username", user.getUsername());
                    userMap.put("email", user.getEmail());
                    userMap.put("role", user.getRole());
                    userMap.put("active", user.isActive());
                    userMap.put("createdAt", user.getCreatedAt());
                    return userMap;
                })
                .collect(Collectors.toList());
            return ResponseEntity.ok(userDTOs);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 状态信息
     * @return 更新结果
     */
    @PutMapping("/messages/users/{id}/status")
    public ResponseEntity<?> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> status) {
        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            user.setActive(status.get("active"));
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * 更新用户角色
     * @param id 用户ID
     * @param roleInfo 角色信息
     * @return 更新结果
     */
    @PutMapping("/messages/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> roleInfo) {
        try {
            User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
            String roleStr = roleInfo.get("role");
            User.UserRole roleEnum;
            try {
                roleEnum = User.UserRole.valueOf(roleStr);
            } catch (Exception e) {
                throw new RuntimeException("无效的角色类型: " + roleStr);
            }
            user.setRole(roleEnum);
            userRepository.save(user);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
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
