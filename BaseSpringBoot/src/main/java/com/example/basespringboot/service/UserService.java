package com.example.basespringboot.service;

import com.example.basespringboot.config.JwtTokenProvider;
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
public class UserService implements UserDetailsService{

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
            return ResponseEntity.status(400).body("User, password không thể để trống !");
        }

        // Get user from db
        User user = userRepository.findByUserName(loginRequest.getUser());
        if (user == null) {
            return ResponseEntity.status(400).body("Không tìm thấy tài khoản !");
        }
        if (user.getUserName().equals(loginRequest.getUser()) && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(loginRequest.getUser()).password(loginRequest.getPassword()).build();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            loginResponse.setAccessToken(jwtTokenProvider.generateToken(usernamePasswordAuthenticationToken));

            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(400).body("Sai tài khoản hoặc mật khẩu !");
        }
    }

    public void signup(RegisterRequest request) throws RuntimeException {
        // Validate
        if (StringUtils.isEmpty(request.getUser())
                || StringUtils.isEmpty(request.getPassword())
                || StringUtils.isEmpty(request.getRole())) {
            throw new NullPointerException("User, password, role không thể để trống !");
        }

        // check admin user
        if (userAdmin.equals(request.getUserAdmin()) && passwordAmin.equals(request.getPasswordAdmin())) {
            // get user from database
            User user = userRepository.findByUserName(request.getUser());
            if (user == null) {
                user = new User();
                user.setUserName(request.getUser());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(request.getRole());
                userRepository.saveAndFlush(user);
            } else {
                throw new RuntimeException("Tài khoản đã tồn tại !");
            }
        } else {
            throw new RuntimeException("Bạn không phải admin !");
        }
    }

    public void remove(RegisterRequest request) throws RuntimeException {
        // Validate
        if (StringUtils.isEmpty(request.getUser())) throw new NullPointerException("User không thể để trống !");

        // check admin user
        if (userAdmin.equals(request.getUserAdmin()) && passwordAmin.equals(request.getPasswordAdmin())) {
            // get user from database
            User user = userRepository.findByUserName(request.getUser());
            if (!Objects.isNull(user)) {
                userRepository.delete(user);
            } else {
                throw new UsernameNotFoundException("Không tìm thấy tài khoản với user name: " + request.getUser());
            }
        } else {
            throw new RuntimeException("Bạn không phải admin !");
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
}