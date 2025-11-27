package org.example.tpgrpc.users.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.tpgrpc.users.domain.User;
import org.example.tpgrpc.users.repository.UserRepository;
import org.example.tpgrpc.users.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(String name, String email) {
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new IllegalStateException("Email déjà utilisé : " + email);
        });

        User user = User.builder()
                .name(name)
                .email(email)
                .build();

        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec id : " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilisateur introuvable avec id : " + id);
        }
        userRepository.deleteById(id);
    }
}