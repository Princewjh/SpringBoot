package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author wjh
 * @create 2021 - 07 - 25 8:49
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine; //导入thymeleaf模板引擎，用于在测试类中使用thymeleaf模板

    @Test
    public void testTextMail(){
        mailClient.sendMail("18813058377@163.com", "dhj","gjgkkgl");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","wjh"); //给模板传递数据

        String content = templateEngine.process("/mail/demo", context);//传递路径和内容，并返回邮件内容

        System.out.println(content);

        mailClient.sendMail("18813058377@163.com","HTML",content); //发送邮件
    }


}
