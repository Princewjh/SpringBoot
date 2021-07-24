package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;


/**
 * @author wjh
 * @create 2021 - 07 - 22 9:17
 */
@Repository("alphaHibernate") //括号中为自定义的bean名
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
