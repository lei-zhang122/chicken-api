package com.chicken.api.dao;

import com.chicken.api.model.AccountHit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountHitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountHit record);

    AccountHit selectByPrimaryKey(Integer id);

    List<AccountHit> selectAll();

    int updateByPrimaryKey(AccountHit record);

}