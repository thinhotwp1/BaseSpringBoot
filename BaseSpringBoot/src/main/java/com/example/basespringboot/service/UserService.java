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
        if(loginRequest.getUser().isEmpty() || loginRequest.getPassword().isEmpty() ){
            return ResponseEntity.status(400).body("User, password can not null !");
        }

        // Get user from db
        User user = userRepository.findByUserName(loginRequest.getUser());
        if (user.getUserName().equals(loginRequest.getUser()) && passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){

            UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(loginRequest.getUser()).password(loginRequest.getPassword()).build();
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            loginResponse.setAccessToken(jwtTokenProvider.generateToken(usernamePasswordAuthenticationToken));

            return ResponseEntity.ok(loginResponse);
        } else {
            return ResponseEntity.status(400).body("User or password wrong!");
        }
    }

    public void signup(RegisterRequest request) throws Exception {
        if(StringUtils.isEmpty(request.getUser())
                || StringUtils.isEmpty(request.getPassword())
                || StringUtils.isEmpty(request.getRole())
        ){
            throw new Exception("User, password, role can not null !");
        }
        if (userAdmin.equals(request.getUserAdmin()) && passwordAmin.equals(request.getPasswordAdmin())) {
            User user = userRepository.findByUserName(request.getUser());
            if (user == null) {
                user = new User();
                user.setUserName(request.getUser());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setRole(request.getRole());
                userRepository.saveAndFlush(user);
            } else {
                throw new Exception("Account is exited !");
            }
        } else {
            throw new Exception("You are not admin !");
        }
    }

    public void remove(RegisterRequest request) throws Exception {
        if(StringUtils.isEmpty(request.getUser())){
            throw new Exception("User can not null !");
        }
        if (userAdmin.equals(request.getUserAdmin()) && passwordAmin.equals(request.getPasswordAdmin())) {
            User user = userRepository.findByUserName(request.getUser());
            if(!Objects.isNull(user)){
                userRepository.delete(user);
            }else{
                throw new Exception("Not found user with username: "+request.getUser());
            }
        } else {
            throw new Exception("You are not admin !");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(username,
                user.getPassword(),
                authorities);
    }
}
