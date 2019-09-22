package com.chicken.api.dao;

import com.chicken.api.model.AccountHit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountHitDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountHit record);

    AccountHit selectByPrimaryKey(Integer id);

    List<AccountHit> selectAll();

    int updateByPrimaryKey(AccountHit record);

    List<AccountHit> selectByAccountHit(AccountHit accountHit);

    List<Map> selectHitUserName(AccountHit accountHit);

    Long selectTodayHitScore(AccountHit accountHit);
}