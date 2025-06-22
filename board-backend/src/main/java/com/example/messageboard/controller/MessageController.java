package com.example.messageboard.controller;

import com.example.messageboard.entity.Message;
import com.example.messageboard.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping
    public Message addMessage(@RequestBody Message message) {
        message.setCreateTime(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @GetMapping
    public List<Message> getMessages() {
        return messageRepository.findAllByOrderByCreateTimeDesc();
    }
}
