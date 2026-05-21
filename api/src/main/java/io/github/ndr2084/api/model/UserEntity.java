package io.github.ndr2084.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private Long githubId;
    private String username;
    private String email;
    private String avatarUrl;

    public Long getGithubId() { return githubId; }

    public void setGithubId(Long githubId) { this.githubId = githubId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    @Override
    public String toString() {
        return "UserEntity{githubId='" + githubId + "', username='" + username + "'}";
    }
}
