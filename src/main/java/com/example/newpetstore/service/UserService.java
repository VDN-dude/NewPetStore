package com.example.newpetstore.service;

import com.example.newpetstore.entity.User;
import com.example.newpetstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User save(User user){
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password){

        Optional<User> byUsername = userRepository.findByUsername(username);

        if (byUsername.isPresent()){

            User user = byUsername.get();

            if (passwordEncoder().matches(password, user.getPassword())) {

                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void update(String username, User user){

        Optional<User> byUsername = userRepository.findByUsername(username);

        if (byUsername.isPresent()){

            User user1 = byUsername.get();
            user.setId(user1.getId());
            userRepository.save(user);
        }
    }

    public void delete(String username){
        userRepository.deleteByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()){
            return byUsername.get();
        }
        throw new UsernameNotFoundException("User by username not found");
    }
}
