package com.example.dacn.controller;

import com.example.dacn.constance.SystemConstance;
import com.example.dacn.requestmodel.SignUpFormRequest;
import com.example.dacn.responsemodel.APIResponse;
import com.example.dacn.services.IOTPService;
import com.example.dacn.services.impl.JavaMailSenderService;
import com.example.dacn.template.MailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/otp")
public class OTPController {

    @Autowired
    private IOTPService OTPService;

    @Autowired
    private JavaMailSenderService javaMailSenderService;

    @Value("${spring.application.mailHTMLPath}")
    public String mailHTMLPath;


    @PostMapping("/generate-otp")
    public ResponseEntity generateOTP(@RequestParam String username) {
        int OTP = this.OTPService.generateOTP(username);
        MailTemplate mailTemplate = new MailTemplate(this.mailHTMLPath);
        Map<String, String> replacements = new HashMap<String, String>();
        replacements.put("username", username);
        replacements.put("OTPNumber", String.valueOf(OTP));
        replacements.put("applicationName", SystemConstance.APPLICATION_NAME);
        String message = mailTemplate.getTemplate(replacements);
        try {
            this.javaMailSenderService.sendOTPMessage(username, "Rental Car: Confirm OTP to sign up", message);
            APIResponse<String> response = new APIResponse<>("Mã xác thực đã được gửi đến email của bạn", HttpStatus.CREATED.getReasonPhrase(), HttpStatus.CREATED.value());
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            APIResponse<String> response = new APIResponse<>("Đã xảy ra lỗi. Vui lòng thử lại", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity validateOtp(@RequestParam String username, @RequestBody int OTPNumber) {
        int serverOTP = this.OTPService.getOTP(username);
        if (serverOTP > 0) {
            if (OTPNumber == serverOTP) {
                this.OTPService.clearOTP(username);
                APIResponse<String> response = new APIResponse<>("Xác thực thành công", HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
                return ResponseEntity.ok(response);
            } else {
                APIResponse<String> response = new APIResponse<>("Mã xác thực không hợp lệ. Vui lòng thử lại", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
                return ResponseEntity.ok(response);
            }
        } else {
            APIResponse<String> response = new APIResponse<>("Mã xác thực không hợp lệ hoặc đã hết hạn. Vui lòng thử lại", HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), HttpStatus.UNPROCESSABLE_ENTITY.value());
            return ResponseEntity.ok(response);
        }
    }
}
