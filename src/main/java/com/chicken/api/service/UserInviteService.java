package com.chicken.api.service;

import com.chicken.api.dao.UserInviteDao;
import com.chicken.api.model.AccountHit;
import com.chicken.api.model.UserInvite;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author zhanglei
 * @date 2019-09-01 21:10
 */
@Service
public class UserInviteService {

    @Autowired
    UserInviteDao userInviteDao;

    @Transactional(rollbackFor = Exception.class)
    public int insert(UserInvite record) {
        return userInviteDao.insert(record);
    }

    public PageInfo<Map> selectHitUserName(UserInvite userInvite, int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了
        PageHelper.startPage(pageNum, pageSize);
        List<Map> userLists = userInviteDao.selectByUserInvite(userInvite);
        PageInfo result = new PageInfo(userLists);
        return result;
    }
}
