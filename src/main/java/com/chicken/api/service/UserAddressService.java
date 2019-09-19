package com.chicken.api.service;

import com.chicken.api.dao.UserAddressDao;
import com.chicken.api.model.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhanglei
 * @date 2019-09-01 21:10
 */
@Service
public class UserAddressService {

    @Autowired
    UserAddressDao userAddressDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return userAddressDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(UserAddress record) {
        return userAddressDao.insert(record);
    }

    public UserAddress selectByPrimaryKey(Integer id) {
        return userAddressDao.selectByPrimaryKey(id);
    }


    public List<UserAddress> selectAll() {
        return userAddressDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(UserAddress record) {
        return userAddressDao.updateByPrimaryKey(record);
    }


    public List<UserAddress> selectByUserAddress(UserAddress userAddress){
        return this.userAddressDao.selectByUserAddress(userAddress);
    }
}
