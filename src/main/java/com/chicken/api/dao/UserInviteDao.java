package com.chicken.api.dao;

import com.chicken.api.model.UserInvite;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserInviteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserInvite record);

    UserInvite selectByPrimaryKey(Integer id);

    List<UserInvite> selectAll();

    int updateByPrimaryKey(UserInvite record);

    List<Map> selectByUserInvite(UserInvite userInvite);
}