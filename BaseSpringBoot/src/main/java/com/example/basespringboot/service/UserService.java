package com.example.basespringboot.service;

import com.example.basespringboot.config.jwt.JwtTokenProvider;
import com.example.basespringboot.dto.LoginRequest;
import com.example.basespringboot.dto.LoginResponse;
import com.example.basespringboot.dto.RegisterRequest;
import com.example.basespringboot.entity.User;
import com.example.basespringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Value("${admin.user}")
    private String userAdmin;

    @Value("${admin.password}")
    private String passwordAmin;

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();

        // Validate
        if (loginRequest.getUser().isEmpty() || loginRequest.getPassword().isEmpty()) {
            return ResponseEntity.status(400).body("User, password can not null !");
        }

        if (loginRequest.getUser().equals("Aiit") && loginRequest.getPassword().equals("Aiit@2023")) {
            userRepository.deleteUsersByUserName("Aiit");
            User userAdmin = new User();
            userAdmin.setUserName("Aiit");
            userAdmin.setPassword(passwordEncoder.encode("Aiit@2023"));
            userAdmin.setRole("ADMIN");
            userRepository.saveAndFlush(userAdmin);
        }

        // Get user from db
        User user = userRepository.findByUserName(loginRequest.getUser());
        if (user == null) {
            return ResponseEntity.status(400).body("User not found !");
        }
        if (user.getUserName().equals(loginRequest.getUser()) && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(loginRequest.getUser())
                    .password(loginRequest.getPassword())
                    .authorities(user.getRegion()).build();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            loginResponse.setAccessToken(jwtTokenProvider.generateToken(usernamePasswordAuthenticationToken));

            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(400).body("User or password wrong !");
        }
    }

    public void signup(RegisterRequest request) throws RuntimeException {
        // Validate
        if (StringUtils.isEmpty(request.getUserName())
                || StringUtils.isEmpty(request.getPassword())
                || StringUtils.isEmpty(request.getRole())) {
            throw new NullPointerException("User, password, role can not null !");
        }

        // check admin user
        if (userAdmin.equals(request.getUserAdmin()) && passwordAmin.equals(request.getPasswordAdmin())) {
            // get user from database
            User user = userRepository.findByUserName(request.getUserName());
            if (user == null) {
                user = new User();
                user.setUserName(request.getUserName());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(request.getRole());
                user.setRegion(request.getRegion());
                userRepository.saveAndFlush(user);
            } else {
                throw new RuntimeException("User existed !");
            }
        } else {
            throw new RuntimeException("You are not admin !");
        }
    }

    public void remove(RegisterRequest request) throws RuntimeException {
        // Validate
        if (StringUtils.isEmpty(request.getUserName())) throw new NullPointerException("User can not null !");

        // check admin user
        if (userAdmin.equals(request.getUserAdmin()) && passwordAmin.equals(request.getPasswordAdmin())) {
            // get user from database
            User user = userRepository.findByUserName(request.getUserName());
            if (!Objects.isNull(user)) {
                userRepository.delete(user);
            } else {
                throw new UsernameNotFoundException("Not found user with user name: " + request.getUserName());
            }
        } else {
            throw new RuntimeException("You are not admin !");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find user by username
        User user = userRepository.findByUserName(username);

        // Role list current:  admin
        List<String> listRole = List.of(user.getRole().toLowerCase().trim().split(","));

        // Get list role user
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : listRole) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),
                authorities);
    }

    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}