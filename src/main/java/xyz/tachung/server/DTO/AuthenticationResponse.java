package xyz.tachung.server.DTO;


import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class AuthenticationResponse {
    private String token;
    private UserInfo userInfo;

    public AuthenticationResponse(String token, String refreshToken, UserInfo userInfo) {
        this.token = token;
        this.userInfo = userInfo;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}
