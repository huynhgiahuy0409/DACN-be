package com.example.dacn.controller;

import com.example.dacn.constance.ErrorMessage;
import com.example.dacn.dto.JWTDTO;
import com.example.dacn.dto.UserDTO;
import com.example.dacn.dto.response.ErrorResponse;
import com.example.dacn.entity.JWTEntity;
import com.example.dacn.entity.UserEntity;
import com.example.dacn.enums.Gender;
import com.example.dacn.enums.OAuthProvider;
import com.example.dacn.enums.Role;
import com.example.dacn.enums.UserStatus;
import com.example.dacn.requestmodel.ForgetPasswordRequest;
import com.example.dacn.requestmodel.LoginRequest;
import com.example.dacn.requestmodel.SignUpRequest;
//import com.example.dacn.requestmodel.SocialUserRequest;
import com.example.dacn.responsemodel.APIResponse;
import com.example.dacn.responsemodel.AuthenticationResponse;
import com.example.dacn.services.CustomUserDetailsService;
import com.example.dacn.services.IHttpHeaderReader;
import com.example.dacn.services.IJWTService;
import com.example.dacn.services.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/auth")
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
    private IUserService socialUserService;

    @Autowired
    private IJWTService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IHttpHeaderReader httpHeaderReader;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        UserDetails userDetails;
        userDetails = this.customUserDetailService.loadUserByUsername(username);
        if (userDetails != null) {
            return ResponseEntity.ok(Boolean.TRUE);
        } else {
            return ResponseEntity.ok(Boolean.FALSE);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity login(@RequestBody @Valid LoginRequest loginRequest, BindingResult bindingResult) {
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(loginRequest.getUsername());
        if (userDetails != null) {
            if (this.userService.checkPassword(userDetails, loginRequest.getPassword())) {
                JWTDTO accTokenDTO = this.modelMapper.map(this.jwtService.createToken(userDetails, "access"), JWTDTO.class);
                JWTDTO refTokenDTO = this.modelMapper.map(this.jwtService.createToken(userDetails, "refresh"), JWTDTO.class);
                UserDTO userDTO = this.userService.getUserDTO(userDetails.getUsername());
                AuthenticationResponse data = new AuthenticationResponse(userDTO, accTokenDTO, refTokenDTO);
                return ResponseEntity.ok(data);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Sai mật khẩu", HttpStatus.BAD_REQUEST));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Người dùng chưa được đăng ký", HttpStatus.BAD_REQUEST));
        }

    }

    @GetMapping("/refresh-token")
    public ResponseEntity refreshTokenCallAPI(@RequestParam String refreshToken) {
        try {
            if (StringUtils.hasText(refreshToken) && this.jwtService.validateToken(refreshToken)) {
                String username = this.jwtService.getUsernameFromToken(refreshToken);
                UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
                JWTDTO accTokenDTO = this.modelMapper.map(this.jwtService.createToken(userDetails, "access"), JWTDTO.class);
                JWTDTO refTokenDTO = this.modelMapper.map(this.jwtService.createToken(userDetails, "refresh"), JWTDTO.class);
                UserDTO userDTO = this.userService.getUserDTO(userDetails.getUsername());
                AuthenticationResponse data = new AuthenticationResponse(userDTO, accTokenDTO, refTokenDTO);
                return ResponseEntity.ok(data);
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Mã không hợp lệ", HttpStatus.BAD_REQUEST));
            }
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Phiên đăng nhập hết hạn", HttpStatus.BAD_REQUEST));
        }
    }

//    @PostMapping("/social-sign-in")
//    public ResponseEntity signInWithGoogle(@RequestBody SocialUserRequest socialUserRequest) {
//        System.out.println(socialUserRequest);
//        String id = socialUserRequest.getId();
//        OAuthProvider provider = socialUserRequest.getProvider();
//        UserDetails userDetails;
//        userDetails = this.customUserDetailService.loadUserByUsername(id);
//        UserDTO user;
//        if (userDetails == null) {
//            List<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
//            roles.add(new SimpleGrantedAuthority(Role.USER.name()));
//            userDetails = new User(id, "", roles);
//            UserEntity newSocialUser = new UserEntity();
//            newSocialUser.setUsername(id);
//            newSocialUser.setPassword("");
//            newSocialUser.setEmail(socialUserRequest.getEmail());
//            newSocialUser.setProvider(provider);
//            newSocialUser.setFullName(socialUserRequest.getName());
//            newSocialUser.setStatus(UserStatus.ACTIVATED);
//            newSocialUser.setRole(Role.USER);
//            user = this.modelMapper.map(this.userService.save(newSocialUser), UserDTO.class);
//        } else {
//            user = this.userService.getUserDTO(userDetails.getUsername());
//        }
//        System.out.println(userDetails);
//        String accessToken = this.jwtService.generateToken(userDetails, "access");
//        Date expiredAccessToken = this.jwtService.getExpirationDateFromToken(accessToken);
//        String refreshToken = this.jwtService.generateToken(userDetails, "refresh");
//        Date expiredRefreshToken = this.jwtService.getExpirationDateFromToken(refreshToken);
//        JWTEntity accessJWTEntity = new JWTEntity(accessToken, expiredAccessToken);
//        JWTEntity refreshJWTEntity = new JWTEntity(refreshToken, expiredRefreshToken);
//        JWTDTO accessJWTDTO = this.modelMapper.map(jwtService.save(accessJWTEntity), JWTDTO.class);
//        JWTDTO refreshJWTDTO = this.modelMapper.map(jwtService.save(refreshJWTEntity), JWTDTO.class);
//        AuthenticationResponse data = new AuthenticationResponse(user, accessJWTDTO, refreshJWTDTO);
//        APIResponse<AuthenticationResponse> response = new APIResponse<>(data, HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
//        return ResponseEntity.ok(response);
//    }


    @PostMapping(value = "/validate-sign-up")
    public ResponseEntity validSignUp(@RequestBody SignUpRequest signUpRequest) {
        boolean isValidPassword = this.userService.checkValidPassword(signUpRequest.getPassword());
        boolean isExistUser = this.userService.checkExistUser(signUpRequest.getUsername());
        if (isExistUser) {
            UserEntity foundUser = this.userService.findByUsername(signUpRequest.getUsername());
            if (foundUser.getStatus().equals(UserStatus.UNACTIVATED)) {
                UserEntity unActiveUser = this.userService.generateUser(signUpRequest, UserStatus.UNACTIVATED);
                this.userService.save(unActiveUser);
                return ResponseEntity.ok("Xác thực thông tin hợp lệ");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản đã tồn tại");
            }
        } else {
            if (isValidPassword) {
                UserEntity unActiveUser = this.userService.generateUser(signUpRequest, UserStatus.UNACTIVATED);
                this.userService.save(unActiveUser);
                return ResponseEntity.ok("Xác thực thông tin hợp lệ");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mật khẩu không hợp lệ");
            }
        }
    }


    @PostMapping(value = "/revoke-token")
    public ResponseEntity revokeToken(HttpServletRequest request, @RequestBody String refreshToken) {
        try {
            String accessToken = this.httpHeaderReader.getTokenFromHeader(request);
            Long removedAccess = this.jwtService.removeByToken(accessToken);
            Long removedRefresh = this.jwtService.removeByToken(refreshToken);
            if (removedAccess.longValue() != -1 && removedAccess.longValue() != -1) {
                APIResponse<String> response = new APIResponse<String>("Thu hồi thành công", HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
                return ResponseEntity.ok(response);
            } else {
                APIResponse<String> response = new APIResponse<String>("Thu hồi thất bại", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            APIResponse<String> response = new APIResponse<String>("Thu hồi thất bại", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/validate-username")
    public ResponseEntity checkActivatedUser(@RequestParam String username) {
        UserEntity foundUser = this.userService.findByUsername(username);
        if (foundUser != null && foundUser.getStatus().equals(UserStatus.ACTIVATED)) {
            return ResponseEntity.ok("Tên tài khoản hợp lệ");
        } else if (foundUser != null && foundUser.getStatus().equals(UserStatus.BANNED)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tên tài khoản đã bị khoá");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tài khoản không tồn tại");
        }
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity changePassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest) {
        UserEntity foundUser = this.userService.findByUsername(forgetPasswordRequest.getUsername());
        if (foundUser != null && foundUser.getStatus().equals(UserStatus.ACTIVATED) && this.userService.checkValidPassword(forgetPasswordRequest.getNewPassword())) {
            foundUser.setPassword(passwordEncoder.encode(forgetPasswordRequest.getNewPassword()));
            this.userService.save(foundUser);
            return ResponseEntity.ok("Đổi mật khẩu thành công");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.US_ERROR);
        }
    }
}
