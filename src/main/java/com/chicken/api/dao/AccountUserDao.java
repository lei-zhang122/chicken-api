package com.chicken.api.dao;


import com.chicken.api.model.AccountUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountUser record);

    AccountUser selectByPrimaryKey(Integer id);

    AccountUser selectByUserId(Integer userId);

    List<AccountUser> selectAll();

    int updateByPrimaryKey(AccountUser record);

}