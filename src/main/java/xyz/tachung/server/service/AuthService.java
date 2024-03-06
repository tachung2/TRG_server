package xyz.tachung.server.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.tachung.server.Entity.User;
import xyz.tachung.server.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public User Signup(User user) {
        User userEntity = userRepository.save(user);
        return userEntity;
    }
}
