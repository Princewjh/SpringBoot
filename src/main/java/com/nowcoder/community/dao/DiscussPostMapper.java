package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wjh
 * @create 2021 - 07 - 24 8:53
 */
@Mapper
public interface DiscussPostMapper { //分页查询，

    //分页查询 ，userId这个参数待定，在后面用
    //offset为页开始的行号
    //limit为每页帖子个数
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //查询帖子的行数 @Param用来给参数取别名（参数名过长的情况）
    //userId这个参数待定，在后面用
    //如果需要动态的拼接一个条件(动态sql,在<if>里使用)，并且方法有且仅有这一个条件，必须取别名 使用@Param
    int selectDiscussPostRows(@Param("userId") int userId);
}
