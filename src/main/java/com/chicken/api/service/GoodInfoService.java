package com.chicken.api.service;

import com.chicken.api.dao.GoodInfoDao;
import com.chicken.api.model.GoodInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodInfoService {

    @Autowired
    GoodInfoDao goodInfoDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return goodInfoDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(GoodInfo record) {
        return goodInfoDao.insert(record);
    }

    public GoodInfo selectByPrimaryKey(Integer id) {
        return goodInfoDao.selectByPrimaryKey(id);
    }


    public List<GoodInfo> selectAll() {
        return goodInfoDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(GoodInfo record) {
        return goodInfoDao.updateByPrimaryKey(record);
    }


}
