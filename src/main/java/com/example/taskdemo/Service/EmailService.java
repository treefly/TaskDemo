package com.example.taskdemo.Service;

import com.example.taskdemo.task.email.SendEmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${server}")
    private String emailServer;

    @Autowired
    public SendEmailUtils emailUtils;

    public  void SendEmail() throws Exception {
        Map map = new HashMap();
        map.put("mailhost","hailun.zhang@dbh.dynabook.com");
        map.put("content","欢迎使用任务协同系统！");
        map.put("sw",true);

        emailUtils.MailInfo(map);
    }
}
