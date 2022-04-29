package se.joce.springv2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.joce.springv2.model.Role;
import se.joce.springv2.model.User;
import se.joce.springv2.repository.RoleRepository;
import se.joce.springv2.repository.UserRepository;

import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Load user from the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("Username not found in the database");
        } else {
            log.info("User found in the database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        //To define the differences between userdetails user and our user
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user in database");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role in database");
        return roleRepository.save(role);
    }

    /**
     * Adding the role to the user, even if the user had some additional before
     * Transactional helps us execute, and saves it in the database (do not have to call the repo)
     */
    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role to user");
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
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

//      If new user entity is registered for the first time, there is no id, thus continue to validate e-mail.
//        Otherwise, write over user entity with given id via save method.
        if (user.getId() == null) {
//        Check that e-mail is unique
            Optional<User> userWithProposedEmail = getUserByEmail(user.getEmail());

            if (userWithProposedEmail.isPresent()) {
                return false;
            }
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public Optional<User> updateUser(Integer id, User user) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) return userOptional;

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
