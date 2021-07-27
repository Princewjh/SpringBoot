package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wjh
 * @create 2021 - 07 - 27 12:47
 */

@Mapper
public interface LoginTicketMapper {

    int insertLoginTicket(LoginTicket loginTicket);


}
