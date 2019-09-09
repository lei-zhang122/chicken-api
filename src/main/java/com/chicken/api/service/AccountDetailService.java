package com.chicken.api.service;

import com.chicken.api.dao.AccountDetailDao;
import com.chicken.api.model.AccountDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhanglei
 * @date 2019-09-01 21:10
 */
@Service
public class AccountDetailService {

    @Autowired
    AccountDetailDao accountDetailDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return accountDetailDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(AccountDetail record) {
        return accountDetailDao.insert(record);
    }

    public AccountDetail selectByPrimaryKey(Integer id) {
        return accountDetailDao.selectByPrimaryKey(id);
    }


    public List<AccountDetail> selectAll() {
        return accountDetailDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(AccountDetail record) {
        return accountDetailDao.updateByPrimaryKey(record);
    }

}
