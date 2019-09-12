package com.chicken.api.service;

import com.chicken.api.dao.WechatUserDao;
import com.chicken.api.model.WechatUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhanglei
 * @date 2019-09-01 21:10
 */
@Service
public class WechatUserService {

    @Autowired
    WechatUserDao wechatUserDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return wechatUserDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(WechatUser record) {
        return wechatUserDao.insert(record);
    }

    public WechatUser selectByPrimaryKey(Integer id) {
        return wechatUserDao.selectByPrimaryKey(id);
    }


    public List<WechatUser> selectAll() {
        return wechatUserDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(WechatUser record) {
        return wechatUserDao.updateByPrimaryKey(record);
    }


    public WechatUser selectByOpenId(String openId) {
        return wechatUserDao.selectByOpenId(openId);
    }

    public List<WechatUser> selectTopFive() {
        return wechatUserDao.selectTopFive();
    }
}
