package com.chicken.api.service;

import com.chicken.api.dao.AccountDetailDao;
import com.chicken.api.model.AccountDetail;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    @Async
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

    public PageInfo<AccountDetail> selectByAccountDetail(AccountDetail accountDetail, int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了
        PageHelper.startPage(pageNum, pageSize);
        List<AccountDetail> userLists = accountDetailDao.selectByAccountDetail(accountDetail);
        PageInfo result = new PageInfo(userLists);
        return result;
    }
}
