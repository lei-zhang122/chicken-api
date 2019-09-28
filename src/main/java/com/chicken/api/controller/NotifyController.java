package com.chicken.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.Dictionary;
import com.chicken.api.service.DictionaryService;
import com.chicken.api.service.GoodOrderService;
import com.chicken.api.util.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author zhanglei
 * @date 2019-09-09 20:31
 */
@RestController
@RequestMapping("/mp")
public class NotifyController {

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    GoodOrderService goodOrderService;

    /**
     * 获取通告信息
     *
     * @return
     */
    @RequestMapping(value = "/newsNotify", method = RequestMethod.GET)
    @ResponseBody
    public Object newsNotify() {

        Dictionary dictionary = new Dictionary();
        dictionary.setDictType("tg");
        dictionary.setStatus("1");
        List<Dictionary> list = this.dictionaryService.selectByDictionary(dictionary);
        JSONArray jsonArray = new JSONArray();
        for (Dictionary d : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", d.getDictName());
            jsonObject.put("content", d.getDictContent());
            jsonArray.add(jsonObject);
        }

        return CallResult.success(jsonArray.toArray());
    }

    /**
     * 获取动画信息
     *
     * @return
     */
    @RequestMapping(value = "/animationNotify", method = RequestMethod.GET)
    @ResponseBody
    public Object animationNotify() {

        Dictionary dictionary = new Dictionary();
        dictionary.setDictType("xjdh");
        dictionary.setStatus("1");
        List<Dictionary> list = this.dictionaryService.selectByDictionary(dictionary);
        JSONArray negativeStatusMap = new JSONArray();
        JSONArray positiveStatusMap = new JSONArray();
        for (Dictionary d : list) {
            String[] detail = d.getDictDetail().split(",");
            String[] content = d.getDictContent().split("@");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("status", d.getDictName());
            jsonObject.put("words", content);
            jsonObject.put("sleep",detail[0]);
            jsonObject.put("during",detail[1]);
            if(d.getDictOrder()==1){
                positiveStatusMap.add(jsonObject);
            }else{
                negativeStatusMap.add(jsonObject);
            }
        }
        JSONObject result = new JSONObject();
        result.put("negativeStatusMap",negativeStatusMap);
        result.put("positiveStatusMap",positiveStatusMap);
        return CallResult.success(result);
    }


    /**
     * 获取兑换商品记录
     *
     * @return
     */
    @RequestMapping(value = "/goodsExchangeNotify", method = RequestMethod.GET)
    @ResponseBody
    public Object goodsExchangeNotify() {

        List<Map> list = this.goodOrderService.selectTopTen();
        JSONArray jsonArray = new JSONArray();
        for (Map m : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", m.get("nick_name") + "兑换了" + m.get("good_name"));
            jsonArray.add(jsonObject);
        }

        return CallResult.success(jsonArray.toArray());
    }
}
