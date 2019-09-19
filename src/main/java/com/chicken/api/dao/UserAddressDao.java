package com.chicken.api.dao;

import com.chicken.api.model.UserAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAddressDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAddress record);

    UserAddress selectByPrimaryKey(Integer id);

    List<UserAddress> selectAll();

    int updateByPrimaryKey(UserAddress record);


    List<UserAddress> selectByUserAddress(UserAddress userAddress);
}