package com.example.taskdemo.Controller;

import com.example.taskdemo.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/email")
public class EmailHelper
{

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public void  SendEmail() throws Exception {
         emailService.SendEmail();
    }
}
