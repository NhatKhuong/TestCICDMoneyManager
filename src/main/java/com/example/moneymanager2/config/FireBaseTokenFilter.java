package com.example.moneymanager2.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FireBaseTokenFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader("Authorization");

        //checks if token is there
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Missing token!");

        FirebaseToken decodedToken = null;
        try {
            //Extracts token from header
            String token = authenticationHeader.substring(7, authenticationHeader.length());
            //verifies token to firebase server
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
        }
        catch (FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Error! "+e.toString());
        }

        //if token is invalid
        if (decodedToken==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid token!");
        }

        chain.doFilter(request,response);
    }
}
