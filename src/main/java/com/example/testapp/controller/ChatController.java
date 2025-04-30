package com.example.testapp.controller;

import com.example.testapp.entities.Chat;
import com.example.testapp.repository.ChatRepository;
import com.example.testapp.services.ChatInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatInterface chatInterface;
    private ChatRepository chatrepo;
    @PostMapping("/add")
    public ResponseEntity<?> addChat(@RequestBody Chat chat) {
        Chat savedChat = chatrepo.save(chat);
        return ResponseEntity.ok(savedChat);
    }

    @GetMapping("/all")
    public List<Chat> getAllChats() {
        return chatInterface.getAllChats();
    }
}
