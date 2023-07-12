package com.example.springrestdemo.controllers;

import com.example.springrestdemo.auth.JwtUtil;
import com.example.springrestdemo.model.User;
import com.example.springrestdemo.model.request.LoginReq;
import com.example.springrestdemo.model.request.RegistrationReq;
import com.example.springrestdemo.model.response.ErrorRes;
import com.example.springrestdemo.model.response.LoginRes;
import com.example.springrestdemo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rest/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserService userService;

    private JwtUtil jwtUtil;
    public AuthController(AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.jwtUtil = jwtUtil;

    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginReq loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            User user = new User(email,"");
            String token = jwtUtil.createToken(user);
            LoginRes loginRes = new LoginRes(email,token);

            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public ResponseEntity register(HttpServletRequest request, HttpServletResponse response, @RequestBody RegistrationReq registrationReq){

        try {
            User user = new User();
            user.setEmail(registrationReq.getEmail());
            user.setFirstName(registrationReq.getFirstName());
            user.setLastName(registrationReq.getLastName());
            user.setPassword(registrationReq.getPassword());
            user.setRole("USER");
            String password = registrationReq.getPassword();
            String encodedPassword = bCryptPasswordEncoder.encode(password);
            System.out.println("encoded password : "+ encodedPassword);
            user.setPassword(encodedPassword);
            User newUser = userService.createUser(user);
            if(newUser == null){
                ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Error creating new user");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            newUser.setPassword("");
            String token = jwtUtil.createToken(newUser);
            LoginRes loginRes = new LoginRes(newUser.getEmail(), token);

            return ResponseEntity.ok(loginRes);
        } catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }
}