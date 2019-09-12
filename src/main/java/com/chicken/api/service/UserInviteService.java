package com.chicken.api.service;

import com.chicken.api.dao.UserInviteDao;
import com.chicken.api.model.UserInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
