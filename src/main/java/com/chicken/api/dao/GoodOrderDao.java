package com.chicken.api.dao;

import com.chicken.api.model.GoodOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodOrderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodOrder record);

    GoodOrder selectByPrimaryKey(Integer id);

    List<GoodOrder> selectAll();

    int updateByPrimaryKey(GoodOrder record);


    List<Map> selectByGoodOrder(GoodOrder goodOrder);
}