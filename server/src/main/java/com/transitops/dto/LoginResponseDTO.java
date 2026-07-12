package com.transitops.dto;


/**
 * Data Transfer Object (DTO) containing LoginResponseDTO data.
 */
public class LoginResponseDTO {
    private String token;
    private Long userId;
    private String email;
    private String role;


    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, Long userId, String email, String role) {
        this.token = token;
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static LoginResponseDTOBuilder builder() {
        return new LoginResponseDTOBuilder();
    }

    public static class LoginResponseDTOBuilder {
        private String token;
        private Long userId;
        private String email;
        private String role;
        public LoginResponseDTOBuilder token(String token) {
            this.token = token;
            return this;
        }
        public LoginResponseDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }
        public LoginResponseDTOBuilder email(String email) {
            this.email = email;
            return this;
        }
        public LoginResponseDTOBuilder role(String role) {
            this.role = role;
            return this;
        }
        public LoginResponseDTO build() {
            return new LoginResponseDTO(this.token, this.userId, this.email, this.role);
        }
    }
}
