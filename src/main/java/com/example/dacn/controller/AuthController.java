package com.example.dacn.controller;

import com.example.dacn.dto.JWTDTO;
import com.example.dacn.dto.UserDTO;
import com.example.dacn.model.JWTEntity;
import com.example.dacn.model.UserEntity;
import com.example.dacn.enums.UserStatus;
import com.example.dacn.requestmodel.LoginRequest;
import com.example.dacn.requestmodel.SignUpFormRequest;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            UserDTO userDTO = this.userService.findByUsernameDTO(userDetails.getUsername());
            AuthenticationResponse data = new AuthenticationResponse(userDTO, accessJWTDTO, refreshJWTDTO);
            APIResponse<AuthenticationResponse> response = new APIResponse<>(data,  "Đăng nhập thành công", HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        }else{
            APIResponse<String> response = new APIResponse<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Sai username hoặc password", HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/validate-sign-up")
    public ResponseEntity validSignUp(@RequestBody SignUpFormRequest signUpRequest) {
        boolean isValidPassword = this.userService.checkValidPassword(signUpRequest.getPassword());
        boolean isExistUsername = this.userService.checkExistUsername(signUpRequest.getUsername());
        if (isValidPassword == true && isExistUsername == false) {
            APIResponse<String> response = new APIResponse<String>("Thông tin hợp lệ", HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
            UserEntity nonActiveUser = new UserEntity();
            nonActiveUser.setUsername(signUpRequest.getUsername());
            nonActiveUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            nonActiveUser.setPhone(signUpRequest.getPhone());
            nonActiveUser.setFullName(signUpRequest.getFullName());
            nonActiveUser.setEmail(signUpRequest.getUsername());
            nonActiveUser.setStatus(UserStatus.UNACTIVATED);
            this.userService.save(nonActiveUser);
            return ResponseEntity.ok(response);
        } else if (isValidPassword == false && isExistUsername == true) {
            APIResponse<String> response = new APIResponse<String>("Username đã tồn tại và password không hợp lệ", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok(response);
        } else {
            if (isExistUsername == true) {
                APIResponse<String> response = new APIResponse<String>("Username đã tồn tại", HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
                return ResponseEntity.ok(response);
            } else {
                APIResponse<String> response = new APIResponse<String>("Password không hợp lệ", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.ok(response);
            }
        }
    }
    @GetMapping("/sign-up")
    public ResponseEntity signUp(@RequestBody SignUpFormRequest signUpRequest) {
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(signUpRequest.getUsername());
        if (userDetails != null && this.userService.checkPassword(userDetails, signUpRequest.getPassword())) {
            UserEntity foundUser = this.userService.findByUsername(signUpRequest.getUsername());
            foundUser.setStatus(UserStatus.ACTIVATED);
            this.userService.save(foundUser);
            APIResponse<String> response = new APIResponse<String>("Tạo tài khoản thành công", HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED.value());
            return ResponseEntity.ok(response);
        }else{
            APIResponse<String> response = new APIResponse<String>("Tài khoản hoặc mật khẩu không hợp lệ", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok(response);
        }
    }
}
