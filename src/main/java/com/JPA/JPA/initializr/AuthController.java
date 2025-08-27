package com.JPA.JPA.initializr;

import com.JPA.JPA.JpaApplication;
//import io.swagger.models.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.ResponseEntity; // added

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final static Logger logger = LoggerFactory.getLogger(JpaApplication.class);
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(
            @RequestBody User user
    ){
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        user = userRepo.save(user);

        String token = jwtUtil.generateToken(user.getUsername());
        return Collections.singletonMap("jwt-token",token);
    }

    @PostMapping("/login")
    public Map<String,Object> loginHandler(
            @RequestBody LoginCreds body
    ){
        try{
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
            authenticationManager.authenticate(authInputToken);
            String token = jwtUtil.generateToken(body.getUsername());
            return Collections.singletonMap("jwt-token",token);
        } catch(AuthenticationException authExc){
            throw new RuntimeException("Invalid username/password.");
        }
    }


    @GetMapping("/info")
    List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @GetMapping("/id")
    public ResponseEntity<UserInfo> getUserById(@RequestParam UUID id){
        Optional<User> userRes = userRepo.findById(id);
        if (userRes.isEmpty())
            ResponseEntity.notFound().build();
        User user = userRes.get();
        UserInfo userInfoById = new UserInfo(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok(userInfoById);
    }

    @GetMapping("/usernameInfo")
    public ResponseEntity<UserInfo> getUserByUsername(@RequestParam UUID id, @RequestParam String username){
        Optional<User> userRes = userRepo.findByUsername(username);
        if (userRes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userRes.get();

        // Return safe DTO (no password)
        UserInfo dto = new UserInfo(user.getId(), user.getUsername(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok(dto);
    }

    // Safe DTO for responses (no password)
    public static record UserInfo(UUID id, String username, String email, String password) {}

}
