package com.ecommerce.project.Controller;

import com.ecommerce.project.Repository.RoleRepository;
import com.ecommerce.project.Repository.UserRepository;
import com.ecommerce.project.model.AppRoles;
import com.ecommerce.project.model.Role;
import com.ecommerce.project.model.User;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.response.UserInfoLoginResponse;
import com.ecommerce.project.security.request.SignupRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.services.JwtService;
import com.ecommerce.project.security.services.UserDetailsImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid  @RequestBody SignupRequest signupRequest){
        if (userRepository.existsByUserName(signupRequest.getUsername())){
            return ResponseEntity.
                    badRequest()
                    .body(new MessageResponse("Error: Username already taken !"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email already exists !"));
        }

        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );
        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null){
            Role userRole = (Role) roleRepository.findByRoleName(AppRoles.ROLE_USER)
                    .orElseThrow(()->new RuntimeException("Error : Role is not found !"));
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role-> {
                switch (role){
                    case "admin":
                        Role adminRole = (Role) roleRepository.findByRoleName(AppRoles.ROLE_ADMIN)
                                .orElseThrow(()->new RuntimeException("Error : Role is not found !"));
                        roles.add(adminRole);
                        break;

                    case "seller":
                        Role sellerRole = (Role) roleRepository.findByRoleName(AppRoles.ROLE_SELLER)
                                .orElseThrow(()->new RuntimeException("Error : Role is not found !"));
                        roles.add(sellerRole);
                        break;

                    default:
                        Role userRole = (Role) roleRepository.findByRoleName(AppRoles.ROLE_USER)
                                .orElseThrow(()->new RuntimeException("Error : Role is not found !"));
                        roles.add(userRole);

                }
            });

        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User Registered Successfully !"));

    }

    @PostMapping("/signin")
    public ResponseEntity<?> doLogin(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate
                    (new  UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        }
        catch (AuthenticationException e){
            Map<String ,Object> map = new HashMap<>();
            map.put("message","Bad Credentials or This user not exists in DB");
            map.put("status",false);
            return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);

        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtService.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        UserInfoLoginResponse response = new
                UserInfoLoginResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                jwtCookie.toString()).body(response);

    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication){
        try {
            if (authentication != null) {
                return authentication.getName();
            }
        }catch (RuntimeException e){
            System.out.println(""+ e);
        }
        return "";
    }


    @GetMapping("/user")
    public ResponseEntity<UserInfoLoginResponse> currentUserDetails(Authentication authentication){
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        UserInfoLoginResponse response = new
                UserInfoLoginResponse(userDetails.getId(),userDetails.getUsername(),roles);

        return ResponseEntity.ok().body(response);

    }

    @PostMapping("/signout")
    public ResponseEntity<?> logout(){
        ResponseCookie cookie = jwtService.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                cookie.toString())
                .body(new MessageResponse("You've been signed out !"));
    }

}
