package com.chicken.api.service;

import com.chicken.api.dao.AccountHitDao;
import com.chicken.api.model.AccountHit;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    @Async
    public void insert(AccountHit record) {
         accountHitDao.insert(record);
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


    public PageInfo<AccountHit> selectByAccountHit(AccountHit accountHit, int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了
        PageHelper.startPage(pageNum, pageSize);
        List<AccountHit> userLists = accountHitDao.selectByAccountHit(accountHit);
        PageInfo result = new PageInfo(userLists);
        return result;
    }

    public List<AccountHit> selectByAccountHitPage(AccountHit accountHit) {
        List<AccountHit> userLists = accountHitDao.selectByAccountHitPage(accountHit);
        return userLists;
    }

    public PageInfo<Map> selectHitUserName(AccountHit accountHit, int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了
        PageHelper.startPage(pageNum, pageSize);
        List<Map> userLists = accountHitDao.selectHitUserName(accountHit);
        PageInfo result = new PageInfo(userLists);
        return result;
    }

    public Long selectTodayHitScore(AccountHit accountHit){
        return accountHitDao.selectTodayHitScore(accountHit);
    }
}
