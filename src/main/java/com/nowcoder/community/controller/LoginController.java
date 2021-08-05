package com.nowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author wjh
 * @create 2021 - 07 - 25 9:23
 */
@Controller
public class LoginController implements CommunityConstant { //注册功能

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    //注入一个值，用@Value
    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    //注册界面
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    //访问登录界面
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    //接收浏览器返回的用户注册信息
    public String register(Model model, User user){ //页面传入值与user对象相匹配，spring MVC会将其自动注入user的属性
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMSg"));
            model.addAttribute("emailMSg",map.get("emailMSg"));
            model.addAttribute("passwordMSg",map.get("passwordMSg"));
            return "/site/register";
        }

    }

    // 激活路径  http://localhost:8080/community/activation/101/code
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) { //@PathVariable :路径变量
        int result = userService.activation(userId, code);
        if(result==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，您的账号可以正常使用");
            model.addAttribute("target","/login");
        }else if(result==ACTIVATION_REPEAT){
            model.addAttribute("msg","无效操作，该账号已经激活过了！");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活失败，您提供的激活码不正确");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    //获取验证码图片的方法
    public void getKaptcha(HttpServletResponse response, HttpSession session){
        //response向浏览器输出图片, 同时服务器需要记住输出的图片，以便下次较验，因此采用session存储信息

        //生成验证码
        String text = kaptchaProducer.createText();//会根据KaptchaConfig中的配置生成一个4位的随机字符串
        BufferedImage image = kaptchaProducer.createImage(text);//生成图片

        // 将验证码文字存入session
        session.setAttribute("kaptcha", text);

        //将图片输出给浏览器

        response.setContentType("image/png"); //告知浏览器返回类型
        try {
            ServletOutputStream os = response.getOutputStream(); //获取字符流
            ImageIO.write(image,"png", os); //输出图片
        } catch (IOException e) {
            logger.error("响应验证码失败：" + e.getMessage());
        }


    }

    //用户登录
    @RequestMapping(path = "/login",method = RequestMethod.POST) //与前一个path相同，但是请求方式不同，所以可以
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, HttpSession session, //获取session,得到验证码
                        HttpServletResponse response){  //response用于得到cookie，将ticket(登录凭证)传给客户端
        //判断验证码是否正确
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }

        //检查账号密码

        //定义登录凭证时间
        int expiredSecondes = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSecondes);
        if(map.containsKey("ticket")){ //成功
            Cookie cookie = new Cookie("ticket", (String)map.get("ticket")); //cookie设置内容
            cookie.setPath(contextPath); // 设置作用范围：整个项目
            cookie.setMaxAge(expiredSecondes); //设置作用时间
            response.addCookie(cookie); //发送
            return "redirect:/index"; //重定向到首页
        }else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";
        }

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) { //得到ticket
        userService.logout(ticket);
        return "redirect:/login"; //重定向时，默认get请求
    }

}
