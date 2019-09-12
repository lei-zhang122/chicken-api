package com.chicken.api.dao;

import com.chicken.api.model.WechatUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WechatUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatUser record);

    WechatUser selectByPrimaryKey(Integer id);

    List<WechatUser> selectAll();

    int updateByPrimaryKey(WechatUser record);


    WechatUser selectByOpenId(String openid);

    List<WechatUser> selectTopFive();
}