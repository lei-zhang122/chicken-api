package com.chicken.api.dao;

import com.chicken.api.model.GoodInfo;
import com.chicken.api.vo.GoodInfoRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodInfo record);

    GoodInfo selectByPrimaryKey(Integer id);

    List<GoodInfo> selectAll();

    int updateByPrimaryKey(GoodInfo record);

    List<GoodInfo> selectByGoodInfo(GoodInfoRequest request);

}