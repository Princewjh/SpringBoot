package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author wjh
 * @create 2021 - 07 - 22 9:37
 */

@Service
//@Scope("prototype") //Spring管理的bean默认为单例，Scope注解用于设置作用范围，prototype设置多个实例
public class AlphaService {

    @Autowired //将alphaDao注入,让AlphaService使用
    private AlphaDao alphaDao;

    public AlphaService(){
        System.out.println("实例化AlphaService");
    }

    //bean的初始化方法(自动调用)
    @PostConstruct //方法会在构造器之后调用
    public void init(){
        System.out.println("初始化AlphaService");
    }

    @PreDestroy //销毁bean的方法 ，在销毁对象之前调用，可用于释放资源（自动调用）
    public void destroy(){
        System.out.println("销毁AlphaService");
    }

    public String find(){
        return alphaDao.select();
    }
}
