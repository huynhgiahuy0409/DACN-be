package com.example.dacn.controller;

import com.example.dacn.constance.ErrorMessage;
import com.example.dacn.constance.SystemConstance;
import com.example.dacn.entity.UserEntity;
import com.example.dacn.enums.OTPType;
import com.example.dacn.enums.UserStatus;
import com.example.dacn.responsemodel.APIResponse;
import com.example.dacn.services.IOTPService;
import com.example.dacn.services.IUserService;
import com.example.dacn.services.impl.JavaMailSenderService;
import com.example.dacn.template.MailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/otp")
public class OTPController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IOTPService registerOTPService;

    @Autowired
    private IOTPService changePasswordOTPService;

    @Autowired
    private JavaMailSenderService javaMailSenderService;

    @Value("${spring.application.mailHTMLPath}")
    public String mailHTMLPath;


    @GetMapping("/generate-otp")
    public ResponseEntity generateOTP(@RequestParam String username, @RequestParam OTPType otpType) {
        try {
            int generatedOTPNumber;
            String subject;
            if (otpType.equals(OTPType.REGISTER)) {
                generatedOTPNumber = this.registerOTPService.generateOTP(username);
                subject = SystemConstance.APPLICATION_NAME.concat(": Confirm OTP to sign up");
            } else {
                generatedOTPNumber = this.changePasswordOTPService.generateOTP(username);
                subject = SystemConstance.APPLICATION_NAME.concat(": Confirm OTP to change your password");
            }
            MailTemplate mailTemplate = new MailTemplate(this.mailHTMLPath);
            Map<String, String> replacements = new HashMap<String, String>();
            replacements.put("username", username);
            replacements.put("OTPNumber", String.valueOf(generatedOTPNumber));
            replacements.put("applicationName", SystemConstance.APPLICATION_NAME);
            String message = mailTemplate.getTemplate(replacements);
            this.javaMailSenderService.sendOTPMessage(username, subject, message);
            return ResponseEntity.ok("Mã xác thực đã được gửi đến email của bạn");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.US_ERROR);
        }
    }

    @GetMapping("/validate-otp")
    public ResponseEntity validateOtp(@RequestParam String username, @RequestParam int OTPNumber, @RequestParam OTPType otpType) {
        int foundOTPNUmber;
        System.out.println("validateOtp");
        if (otpType.equals(OTPType.REGISTER)) {
            foundOTPNUmber = this.registerOTPService.getOTP(username);
        } else {
            foundOTPNUmber = this.changePasswordOTPService.getOTP(username);
        }
        try {
            if (foundOTPNUmber > 0) {
                if (OTPNumber == foundOTPNUmber) {
                    if (otpType.equals(OTPType.REGISTER)) {
                        UserEntity foundUser = this.userService.findByUsername(username);
                        foundUser.setStatus(UserStatus.ACTIVATED);
                        this.userService.save(foundUser);
                        this.registerOTPService.clearOTP(username);
                    } else {
                        this.changePasswordOTPService.clearOTP(username);
                    }
                    return ResponseEntity.ok("Xác thực thành công");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Sai mã xác thực");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Mã xác thực không hợp lệ hoặc đã hết hạn");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorMessage.US_ERROR);
        }

    }

    @GetMapping(value = "/forget-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody String username) {
        if (!StringUtils.isEmpty(username)) {
            String refactorUsername = StringUtils.trimAllWhitespace(username);
            UserEntity foundUser = this.userService.findByUsername(refactorUsername);
            if (foundUser != null) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(false);
    }
}
