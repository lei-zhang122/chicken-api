package com.chicken.api.dao;

import com.chicken.api.model.AccountSigned;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountSignedDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountSigned record);

    AccountSigned selectByPrimaryKey(Integer id);

    List<AccountSigned> selectAll();

    int updateByPrimaryKey(AccountSigned record);

}