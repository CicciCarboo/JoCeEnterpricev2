package se.joce.springv2.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.joce.springv2.model.User;
import se.joce.springv2.repository.UserRepository;
import se.joce.springv2.security.UserPrincipal;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        return userPrincipal;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllAdmin() {
        List<User> allAdminUsers = userRepository.getAllAdmin();
        return allAdminUsers;
    }

    @Override
    public boolean canRegisterNewUser(User user) {
        System.out.println("Running canRegisterUser. with user: " + user.getUsername());

        if (user.getId() == null) {
            System.out.println("user.getId() = " + user.getId());
            Optional<User> userWithProposedEmail = getUserByEmail(user.getEmail());

            if (userWithProposedEmail.isPresent()) {
                System.out.println("userWithProposedEmail.isPresent(). Abort save user.");
                return false;
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        System.out.println("User " + user.getUsername() + " saved in DB.");
        return true;
    }

    @Override
    @Transactional
    public Optional<User> updateUser(Integer id, User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) return userOptional;

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userOptional;
    }

    @Override
    public String deleteUserById(Integer id) {
        try {
            userRepository.deleteById(id);
            return "Successfully deleted user with id: " + id;
        } catch (EmptyResultDataAccessException e) {
            System.out.println("User with id not valid " + e);
            return "User id: " + id + " is not valid. Error message: \"" + e + "\".";
        }
    }

}
