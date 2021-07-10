package com.example.taskdemo.task.email;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Data
@Component
public class SendEmailUtils {

    // 指定发件人电子邮箱
    @Value("${mail.sender}")
    private  String from;
    //private static String from = "609722031@qq.com";
    // 指定发送邮件的主机为
    @Value("${server}")
    private  String host;
    //private static String host = "smtp.qq.com";
    //指定授权码
    @Value("${mail.accredit-code}")
    private  String accreditCode;
    //private static String accreditCode ="ijokzkgtjhqjbdgc";


    private String content ="<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Document</title>\n" +
            "    <style>\n" +
            "        * {\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "\n" +
            "        #email-box {\n" +
            "            width: 700px;\n" +
            "            padding: 30px;\n" +
            "        }\n" +
            "\n" +
            "        .base-mess>* {\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "\n" +
            "        .email-mess>* {\n" +
            "            margin-bottom: 10px;\n" +
            "        }\n" +
            "\n" +
            "        .email-mess,\n" +
            "        .email-down {\n" +
            "            margin-top: 10px;\n" +
            "        }\n" +
            "\n" +
            "        .email-top {\n" +
            "            margin-top: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .email-down>* {\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "\n" +
            "        .email-down>.down-underLine {\n" +
            "            text-decoration: underline;\n" +
            "        }\n" +
            "\n" +
            "        .contain p {\n" +
            "            font-size: 16px;\n" +
            "            font-family: Calibri;\n" +
            "            color: black;\n" +
            "            margin-top: 10px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "    <div id=\"email-box\" class=\"contain\">\n" +
            "        <p>Hi %s</p>\n" +
            "        <br>\n" +
            "        <p>%s is inviting you to attend a dynaConnect meeting.</p>\n" +
            "        <p>[Topic] %s</p>\n" +
            "        <p>[Time] %s</p>\n" +
            "        <p>[Invitation code] %s</p>\n" +
            "        <p>[Description] %s</p>\n" +
            "        <p>You can join the meeting from Windows, iOS or Android.</p>\n" +
            "        <p>Application installation package can be get from below links:</p>\n" +
            "        <div class=\"email-down\" style=\"font-size:16px\">\n" +
            "            <a href=\"http://%s\">Windows (local),</a>\n" +
            "            <a href=\"http://%s\">Android (local),</a>\n" +
            "            <a href=\"%s\">Android (Google Play),</a>\n" +
            "            <a href=\"%s\">iOS (Apple Store)</a>\n" +
            "        </div>\n" +
            "        <br>\n" +
            "        <p>Thanks!</p>\n" +
            "        <p>dynaConnect Team</p>\n" +
            "    </div>\n" +
            "</body>\n" +
            "\n" +
            "</html>";

    public  void MailInfo(Map params) throws Exception {
        /** 1、邮箱必要信息初始化 **/
        // 收件人电子邮箱
        String to = params.get("mailhost").toString();
        //指定文件内容
        //String content = params.get("content").toString();
        //是否含有附件: 是：true  否：false
        Boolean sw = (Boolean) params.get("sw");

        /** 2、系统属性设置 **/
        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器类型
        properties.setProperty("mail.smtp.host", host);
        //用户认证
        properties.put("mail.smtp.auth", "true");
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, accreditCode); //发件人邮件用户名、授权码
            }
        });

        /** 3、文件内容设置 **/
        // 创建默认的 MimeMessage 对象
        MimeMessage message = new MimeMessage(session);
        // Set From: 头部头字段
        message.setFrom(new InternetAddress(from));
        // Set To: 头部头字段
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // Set Subject: 设置邮件主题
        message.setSubject("任务协同提示");
        //设置发送日期
        message.setSentDate(new Date());
        //设置邮件消息体
        MimeMultipart msgMultipart = new MimeMultipart("mixed");//混合的组合关系
        //设置邮件的mine消息体
        message.setContent(msgMultipart);
        // 设置邮件正文内容
        //message.setText("欢迎使用任务协同系统！");
        MimeBodyPart htmlPart = new MimeBodyPart();

        htmlPart.setContent(content,"text/html;charset=UTF-8");

        msgMultipart.addBodyPart(htmlPart);
        if (sw) {
            //附件
            MimeBodyPart attach = new MimeBodyPart();
            //把文件，添加到附件中
            //数据源
            FileDataSource ds = new FileDataSource(new File("C:\\Users\\25877\\Desktop\\cpu.txt"));
            //数据处理器
            DataHandler dh = new DataHandler(ds);
            //设置附件的数据
            attach.setDataHandler(dh);
            //设置附件的文件名
            attach.setFileName(MimeUtility.encodeText(dh.getName()));
            msgMultipart.addBodyPart(attach);
        }
        message.saveChanges();
        // 发送消息
        Transport.send(message, message.getAllRecipients());
        System.out.println("Sent message successfully....from runoob.com");
    }

    public static void main(String[] args) throws Exception {

    }

}
