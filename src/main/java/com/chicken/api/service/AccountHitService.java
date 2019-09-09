package com.chicken.api.service;

import com.chicken.api.dao.AccountHitDao;
import com.chicken.api.model.AccountHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhanglei
 * @date 2019-09-01 21:10
 */
@Service
public class AccountHitService {

    @Autowired
    AccountHitDao accountHitDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return accountHitDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(AccountHit record) {
        return accountHitDao.insert(record);
    }

    public AccountHit selectByPrimaryKey(Integer id) {
        return accountHitDao.selectByPrimaryKey(id);
    }


    public List<AccountHit> selectAll() {
        return accountHitDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(AccountHit record) {
        return accountHitDao.updateByPrimaryKey(record);
    }


}
