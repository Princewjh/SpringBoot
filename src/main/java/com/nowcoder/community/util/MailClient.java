package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * @author wjh
 * @create 2021 - 07 - 25 0:53
 */

@Component
public class MailClient {
    //记录日志
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    //spring管理，直接注入
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from; //发件人

    //实现发送邮件任务
    public void sendMail(String to, String subject, String content){
        try{
            MimeMessage message = mailSender.createMimeMessage(); //构建对象
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from); //设置发件人
            helper.setTo(to); //设置收件人
            helper.setSubject(subject); //设置主题
            helper.setText(content,true); //设置内容,支持html文本
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
        }


    }

}
