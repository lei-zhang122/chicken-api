package com.chicken.api.service;

import com.chicken.api.dao.DictionaryDao;
import com.chicken.api.model.Dictionary;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhanglei
 * @date 2019-09-01 21:10
 */
@Service
public class DictionaryService {

    @Autowired
    DictionaryDao dictionaryDao;

    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Integer id) {
        return dictionaryDao.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public int insert(Dictionary record) {
        return dictionaryDao.insert(record);
    }

    public Dictionary selectByPrimaryKey(Integer id) {
        return dictionaryDao.selectByPrimaryKey(id);
    }


    public List<Dictionary> selectAll() {
        return dictionaryDao.selectAll();
    }


    @Transactional(rollbackFor = Exception.class)
    public int updateByPrimaryKey(Dictionary record) {
        return dictionaryDao.updateByPrimaryKey(record);
    }

    public List<Dictionary> selectByDictionary(Dictionary dictionary){
        return dictionaryDao.selectByDictionary(dictionary);
    }

}
