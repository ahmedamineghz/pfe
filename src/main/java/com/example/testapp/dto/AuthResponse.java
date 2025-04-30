package com.example.testapp.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private String token;
    private String role;
    private String username;
    private Long id;

    public AuthResponse(boolean success, String message, String token, String role, String username, Long id) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.role = role;
        this.username = username;
        this.id = id;

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
