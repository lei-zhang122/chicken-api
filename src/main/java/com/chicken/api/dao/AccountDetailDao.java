package com.chicken.api.dao;

import com.chicken.api.model.AccountDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountDetailDao {
    int deleteByPrimaryKey(Integer id);

    int insert(AccountDetail record);

    AccountDetail selectByPrimaryKey(Integer id);

    List<AccountDetail> selectAll();

    int updateByPrimaryKey(AccountDetail record);

    List<AccountDetail> selectByAccountDetail(AccountDetail accountDetail);
 }