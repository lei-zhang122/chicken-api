package com.chicken.api.service;

import com.chicken.api.dao.GoodOrderDao;
import com.chicken.api.model.GoodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodOrderService {

    @Autowired
    GoodOrderDao goodOrderDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return goodOrderDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(GoodOrder record) {
        return goodOrderDao.insert(record);
    }

    public GoodOrder selectByPrimaryKey(Integer id) {
        return goodOrderDao.selectByPrimaryKey(id);
    }


    public List<GoodOrder> selectAll() {
        return goodOrderDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(GoodOrder record) {
        return goodOrderDao.updateByPrimaryKey(record);
    }

}
