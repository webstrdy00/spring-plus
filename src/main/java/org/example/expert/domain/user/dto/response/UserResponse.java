package org.example.expert.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse implements Serializable {

    private Long id;
    private String email;
    private String nickname;

    @JsonCreator
    public UserResponse(
            @JsonProperty("id") Long id,
            @JsonProperty("email") String email,
            @JsonProperty("nickname") String nickname
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
