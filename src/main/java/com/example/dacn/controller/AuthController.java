package com.example.dacn.controller;

import com.example.dacn.dto.JWTDTO;
import com.example.dacn.dto.UserDTO;
import com.example.dacn.model.JWTEntity;
import com.example.dacn.requestmodel.LoginRequest;
import com.example.dacn.responsemodel.APIResponse;
import com.example.dacn.responsemodel.AuthenticationResponse;
import com.example.dacn.services.CustomUserDetailsService;
import com.example.dacn.services.IJWTService;
import com.example.dacn.services.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomUserDetailsService customUserDetailService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IJWTService JWTService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String usernameErrorMessage = bindingResult.getFieldError("username").getDefaultMessage();
            String passwordErrorMessage = bindingResult.getFieldError("password").getDefaultMessage();
            if(usernameErrorMessage != null || passwordErrorMessage != null){
                APIResponse<String> response = new APIResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Username hoặc password không được để trống", HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.ok(response);
            }
        }
        UserDetails userDetails;
        userDetails = this.customUserDetailService.loadUserByUsername(loginRequest.getUsername());
        if (userDetails != null && this.userService.checkPassword(userDetails, loginRequest.getPassword())) {
            // set UserAuthentication for application to perform other tasks in your application.
            Authentication userAuthentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            // create JWT
            String accessToken = this.JWTService.generateToken(userDetails, "access");
            Date expiredAccessToken = this.JWTService.getExpirationDateFromToken(accessToken);
            String refreshToken = this.JWTService.generateToken(userDetails, "refresh");
            Date expiredRefreshToken = this.JWTService.getExpirationDateFromToken(refreshToken);
            JWTEntity accessJWTEntity = new JWTEntity(accessToken, expiredAccessToken);
            JWTEntity refreshJWTEntity = new JWTEntity(refreshToken, expiredRefreshToken);
            JWTDTO accessJWTDTO = JWTService.save(accessJWTEntity);
            JWTDTO refreshJWTDTO = JWTService.save(refreshJWTEntity);
            UserDTO userDTO = this.userService.findByUsername(userDetails.getUsername());
            AuthenticationResponse data = new AuthenticationResponse(userDTO, accessJWTDTO, refreshJWTDTO);
            APIResponse<AuthenticationResponse> response = new APIResponse<>(data,  "Đăng nhập thành công", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        }else{
            APIResponse<String> response = new APIResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Sai username hoặc password", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok(response);
        }
    }
}
