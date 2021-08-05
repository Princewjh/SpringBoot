package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot.";
    }

    @RequestMapping("/data")
    @ResponseBody
    public String getData(){
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response){ //从DispatcherServlet获取请求和响应对象（底层了解）
        //获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> headerNames = request.getHeaderNames(); //请求的消息头
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);

        }
        //请求体
        System.out.println(request.getParameter("code"));

        //返回响应数据
        response.setContentType("text/html;charset=utf-8"); //返回html文件
        try (
                PrintWriter writer = response.getWriter();//获取输出流
                ){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //获取请求或响应数据（封装方式）

    //处理请求数据
    //GET请求
    // /student?current=1&limit=20 分页批量查询学生,采用？方式
    @RequestMapping(path = "/students", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "10")int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // /student/123 查询单个学生，参数作为路径的一部分
    @RequestMapping(path = "/students/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){ //@PathVariable :路径变量
        System.out.println(id);
        return "a student";
    }

    // POST请求 浏览器向服务器提交数据
    //GET也可以传数据，但是数据会直接显示在url上，且url不会无限长，可能存不下
    @RequestMapping(path="/student",method = RequestMethod.POST)
    @ResponseBody //不加该注解返回html
    public String saveStudent(String name, int age){ //属性与表单一致即可收到数据
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //响应HTML数据
    //两种方法 getTeacher getSchool

    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        //ModelAndView 返回model和view数据
        ModelAndView mav = new ModelAndView();
        //添加变量和数据
        mav.addObject("name","张三");
        mav.addObject("age",30);
        //设置模板路径和名称
        mav.setViewName("/demo/view"); //路径位于templates下,省略html,view.html -- > view
        return mav;
    }

    @RequestMapping(path = "/school",method = RequestMethod.GET)
    public String getSchool(Model model){ //返回值为String时，返回的是模板引擎(view.html的路径)
        //model为DispatcherServlet调用方法时自动实例化并传递的对象
        //DispatcherServlet持有该对象的引用
        //与上一个方法getTeacher类似, getTeacher 是直接得到model和view对象
        //此时将model装到方法参数中，最后返回view
        model.addAttribute("name","北京大学");
        model.addAttribute("age",110);

        return "/demo/view";
    }

    //响应JSON数据（异步请求：当前网页不刷新，但访问了服务器）
    // Java对象 --> JSON字符串(给浏览器) --> JS对象

    @RequestMapping(path = "/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp() {
        //DispatcherServlet调用该方法时，由于注解 @ResponseBody 和返回类型 Map 的原因，会自动将Map转换为JSON,发送给浏览器
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000.00);
        return emp;
    }

    @RequestMapping(path = "/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps() {
        //返回多个员工 JSON格式 返回Map的list
        List<Map<String,Object>> list = new ArrayList<>();

        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","李四");
        emp.put("age",24);
        emp.put("salary",9000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","王五");
        emp.put("age",24);
        emp.put("salary",10000.00);
        list.add(emp);

        return list;
    }

    //cookie示例

    @RequestMapping(path = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    //模拟浏览器产生一个cookie的过程
    public String setCookie(HttpServletResponse response) { //cookie需要存给response对象
        //创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        //设置cookie的生效范围
        cookie.setPath("/community/alpha");
        //cookie默认存在浏览器中，存于内存中，浏览器关掉就消失了
        //给cookie设置生存时间后，它会存在硬盘中，长期有效，直到超过生存时间
        cookie.setMaxAge(60*10); //单位：s
        // 发送cookie，将其放到response的头中
        response.addCookie(cookie);

        return "set cookie";


    }

    @RequestMapping(path = "/cookie/get",method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("code") String code){ //@CookieValue("code") 从cookie中取键为code的那个值
        System.out.println(code);
        return "get cookie";
    }

    //session示例
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session){ //Spring MVC会自动注入session
        session.setAttribute("id",1);
        session.setAttribute("name","Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get",method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session){
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    //ajax示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0,"操作成功");
    }

}
