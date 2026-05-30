package io.github.ndr2084.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
public class UserEntity {


    @Id @Getter @Setter private String Id;
    @Getter @Setter private String username;
    @Getter @Setter private String name;
    @Getter @Setter private String email;
    @Getter @Setter private String avatarUrl;

    @Override
    public String toString() {
        return "UserEntity{githubId='" + Id + "', username='" + username + "'}";
    }
}
