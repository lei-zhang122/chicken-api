package com.chicken.api.service;

import com.chicken.api.dao.GoodOrderDao;
import com.chicken.api.model.GoodOrder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class GoodOrderService {

    @Autowired
    GoodOrderDao goodOrderDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return goodOrderDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Async
    public void insert(GoodOrder record) {
        goodOrderDao.insert(record);
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

    public PageInfo<Map> selectByGoodOrder(GoodOrder goodOrder, int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了
        PageHelper.startPage(pageNum, pageSize);
        List<Map> userLists = goodOrderDao.selectByGoodOrder(goodOrder);
        PageInfo result = new PageInfo(userLists);
        return result;
    }

    public List<Map> selectByGoodOrderDetail(GoodOrder record){
        return goodOrderDao.selectByGoodOrderDetail(record);
    }
}
