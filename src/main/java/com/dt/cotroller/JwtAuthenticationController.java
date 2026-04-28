	
package com.dt.cotroller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dt.entity.JwtResponse;
import com.dt.entity.Users;
import com.dt.jwt.JwtHelper;
import com.dt.payloads.UsersDTO;
import com.dt.service.CustomUserDetailsService;

import lombok.AllArgsConstructor;



@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class JwtAuthenticationController {

    
    @Autowired
    private AuthenticationManager authenticationManager;
  
    @Autowired
    private JwtHelper helper;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;


    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> login(@RequestBody Users request) throws Exception {
    	  
 
               authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken
                                 (request.getUsername(),
                		           request.getPassword()));
          

           final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
           final String jwt = helper.generateToken(userDetails);

           List<String> roles = userDetails.getAuthorities().stream()
        		    .map(GrantedAuthority::getAuthority)
        		    .collect(Collectors.toList());
           
           return ResponseEntity.ok(new JwtResponse(request.getUsername(), jwt));
       }

    
    @GetMapping("/currentuser/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // typically email or username
        Users user = userDetailsService.getUserByUsername(username); // custom logic
        
        
        return ResponseEntity.ok(Map.of(
        	    "name", user.getEmpName(),
        	    "email", user.getUsername()
        	));
    }
    
   

}
