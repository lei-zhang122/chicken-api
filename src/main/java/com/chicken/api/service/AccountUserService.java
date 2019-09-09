package com.chicken.api.service;

import com.chicken.api.dao.AccountUserDao;
import com.chicken.api.model.AccountUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhanglei
 * @date 2019-09-01 21:10
 */
@Service
public class AccountUserService {

    @Autowired
    AccountUserDao accountUserDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return accountUserDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(AccountUser record) {
        return accountUserDao.insert(record);
    }

    public AccountUser selectByPrimaryKey(Integer id) {
        return accountUserDao.selectByPrimaryKey(id);
    }


    public List<AccountUser> selectAll() {
        return accountUserDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(AccountUser record) {
        return accountUserDao.updateByPrimaryKey(record);
    }

}
