package xyz.tachung.server.DTO;


import lombok.Data;

@Data
public class UserInfo { // 유저 정보(name, email, password)
    private String name;
    private String email;

    public UserInfo(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
