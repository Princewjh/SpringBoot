package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author wjh
 * @create 2021 - 07 - 27 12:47
 */

@Mapper
public interface LoginTicketMapper { //这里采用注解的方法去声明mapper中方法对应的sql语句（两种方式:注解和xml）

    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id") //让id字段自动生成
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket = #{ticket} ",
            "<if test=\"ticket!=null\">",
            "and 1=1 ",
            "</if>",
            "</script>"
    })//scrip标签用于写动态sql的标识，
    int updateStatus(String ticket, int status);




}
