package com.m2t.hachikodictionary.service;

import com.m2t.hachikodictionary.config.JWTService;
import com.m2t.hachikodictionary.dto.AuthenticationResponse;
import com.m2t.hachikodictionary.dto.LoginRequest;
import com.m2t.hachikodictionary.dto.RegistrationRequest;
import com.m2t.hachikodictionary.dto.Response;
import com.m2t.hachikodictionary.exception.*;
import com.m2t.hachikodictionary.model.Account;
import com.m2t.hachikodictionary.model.Role;
import com.m2t.hachikodictionary.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final MailService mailService;
    private final ConfirmationService confirmationService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationService(AccountRepository accountRepository, AccountService accountService,
                                 JWTService jwtService, AuthenticationManager authenticationManager,
                                 PasswordEncoder passwordEncoder, MailService mailService,
                                 ConfirmationService confirmationService) {
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.confirmationService = confirmationService;
    }

    public Response register(RegistrationRequest registrationRequest) {
        if(!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new PasswordsDoNotMatchException("Passwords do not match.");
        }
        if(accountRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists.");
        }
        if(accountRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        try {
            Account account = new Account(
                    registrationRequest.getUsername(),
                    passwordEncoder.encode(registrationRequest.getPassword()),
                    registrationRequest.getEmail(),
                    Role.USER
            );

            accountRepository.save(account);

            AuthenticationResponse authResponse = jwtService.generateToken(account);

            String token = confirmationService.create(registrationRequest.getEmail());
            mailService.sendConfirmationEmail(registrationRequest, token);

            Response response = new Response(true, "Registration successful.", authResponse);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

    public Response login(LoginRequest loginRequest) throws Exception {
        try {
            Account user = accountService.loadUserByEmail(loginRequest.getEmail());
            if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new InvalidCredentialsException("Invalid credentials.");
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            Account account = accountRepository.findAccountByEmail(loginRequest.getEmail());
            AuthenticationResponse authResponse = jwtService.generateToken(account);
            Response response = new Response(true, "Login successful.", authResponse);
            return response;
        } catch (AccountNotFoundException e) {
            throw new AccountNotFoundException("Account not found.");
        } catch (InvalidCredentialsException e) {
            throw new InvalidCredentialsException("Invalid credentials.");
        } catch (Exception e) {
            System.out.println("exception happened: " + e);
            throw new Exception("Login failed: " + e.getMessage());
        }


    }

    public Response refreshToken(String token) {
        try {
            String username = jwtService.extractUsername(token);
            Account account = accountService.loadUserByUsername(username);
            if(!jwtService.isTokenValid(token, account)) {
                throw new InvalidTokenException("Token is invalid.");
            }
            AuthenticationResponse authResponse = jwtService.generateToken(account);
            Response response = new Response(true, "Token refreshed.", authResponse);
            return response;
        }
        catch (InvalidTokenException e) {
            throw new InvalidTokenException("Token is invalid.");
        }
        catch (Exception e) {
            throw new RuntimeException("Token refresh failed: " + e.getMessage());
        }

    }
}