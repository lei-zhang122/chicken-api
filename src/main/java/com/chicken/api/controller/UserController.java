package com.chicken.api.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chicken.api.model.GoodOrder;
import com.chicken.api.service.GoodOrderService;
import com.chicken.api.service.RedisService;
import com.chicken.api.util.CallResult;
import com.chicken.api.util.CodeEnum;
import com.chicken.api.util.ContantUtil;
import com.chicken.api.util.DateUtil;
import com.chicken.api.vo.UserRequest;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author zhanglei
 * @date 2019-09-09 20:00
 */
@RestController
@RequestMapping("/mp")
public class UserController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RedisService redisService;

    @Autowired
    GoodOrderService goodOrderService;

    @Autowired
    HttpServletRequest request;


    /**
     * 获取今天已经获得积分数量
     *
     * @return
     */
    @RequestMapping(value = "/integralCount", method = RequestMethod.POST)
    @ResponseBody
    public Object integralCount(@RequestBody UserRequest userRequest) {

        String sessionId = request.getHeader("sessionId");
        if (!isLogin(sessionId)) {
            return CallResult.fail(CodeEnum.LOGIN_OUT_TIME.getCode(), CodeEnum.LOGIN_OUT_TIME.getMsg());
        }

        if (StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Object userId = redisService.get(ContantUtil.OPEN_ID.concat(userRequest.getOpenid()));
        if (null == userId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            userRequest.setUserId(userId.toString());
        }

        //当期那时间
        String now = DateUtil.getSpecifiedDay("yyyy-MM-dd", 0);

        Object gainScoreObj = redisService.get(ContantUtil.GAIN_SCORE.concat(now).concat(":").concat(userRequest.getUserId()));
        Double gainScore = 0.0;
        if (null != gainScoreObj) {
            gainScore = Double.valueOf(gainScoreObj.toString());
        }

        //每天最大分值
        Object maxScore = redisService.get(ContantUtil.MAX_SOCRE_DAY);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("score", gainScore);
        jsonObject.put("maxScore", maxScore);
        jsonObject.put("differentScore", Double.valueOf(maxScore.toString()) - gainScore);

        return CallResult.success(jsonObject);
    }


    /**
     * 获取商品兑换记录
     *
     * @return
     */
    @RequestMapping(value = "/goodExchangeList", method = RequestMethod.POST)
    @ResponseBody
    public Object goodExchangeList(@RequestBody UserRequest userRequest) {

        String sessionId = request.getHeader("sessionId");
        if (!isLogin(sessionId)) {
            return CallResult.fail(CodeEnum.LOGIN_OUT_TIME.getCode(), CodeEnum.LOGIN_OUT_TIME.getMsg());
        }

        if (StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Object userId = redisService.get(ContantUtil.OPEN_ID.concat(userRequest.getOpenid()));
        if (null == userId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            userRequest.setUserId(userId.toString());
        }

        Integer pageNum = ContantUtil.DEFAULT_PAGE_NUM;
        if (null != userRequest.getCurrentPage() && !"0".equals(userRequest.getCurrentPage())) {
            pageNum = Integer.valueOf(userRequest.getCurrentPage());
        }

        Integer pageSize = ContantUtil.DEFAULT_PAGE_SIZE;
        if (null != userRequest.getPageSize() && !"0".equals(userRequest.getPageSize())) {
            pageSize = Integer.valueOf(userRequest.getPageSize());
        }

        //查询兑换信息
        GoodOrder goodOrder = new GoodOrder();
        goodOrder.setUserId(Integer.valueOf(userRequest.getUserId()));
        goodOrder.setStatus("1");
        PageInfo<Map> result = this.goodOrderService.selectByGoodOrder(goodOrder, pageNum, pageSize);
        JSONObject returnJson = new JSONObject();
        returnJson.put("countPage", result.getPages());
        returnJson.put("currentPage", result.getPageNum());
        if (result.getList().size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (Map m : result.getList()) {
                JSONObject object = new JSONObject();
                String date = m.get("exchange_time").toString();
                object.put("exchangeTime", DateUtil.currentYYYYMMDDHHmmssWithSymbol(date));
                object.put("goodName", m.get("good_name"));
                object.put("goodImg", m.get("good_img"));
                object.put("goodType", m.get("good_type"));
                object.put("orderNum", m.get("order_num"));
                object.put("expressName", m.get("express_name"));
                object.put("score", m.get("good_down_virtual"));
                object.put("expressNum", m.get("express_num"));
                String status = m.get("exchange_status").toString();
                if (status.equals("1")) {
                    object.put("exchangeStatus", "已下单");
                } else if (status.equals("2")) {
                    object.put("exchangeStatus", "已发货");
                } else if (status.equals("3")) {
                    object.put("exchangeStatus", "已完成");
                }
                jsonArray.add(object);
            }

            returnJson.put("data", jsonArray.toArray());
        } else {
            returnJson.put("data", null);
        }

        return CallResult.success(returnJson);
    }

    /**
     * 获取商品兑换记录
     *
     * @return
     */
    @RequestMapping(value = "/goodExchangeDetail", method = RequestMethod.POST)
    @ResponseBody
    public Object goodExchangeDetail(@RequestBody UserRequest userRequest) {

        String sessionId = request.getHeader("sessionId");
        if (!isLogin(sessionId)) {
            return CallResult.fail(CodeEnum.LOGIN_OUT_TIME.getCode(), CodeEnum.LOGIN_OUT_TIME.getMsg());
        }

        if (StringUtils.isBlank(userRequest.getOpenid())) {
            return CallResult.fail(CodeEnum.LACK_PARAM.getCode(), CodeEnum.LACK_PARAM.getMsg());
        }

        Object userId = redisService.get(ContantUtil.OPEN_ID.concat(userRequest.getOpenid()));
        if (null == userId) {
            return CallResult.fail(CodeEnum.NO_FIND_USER.getCode(), CodeEnum.NO_FIND_USER.getMsg());
        } else {
            userRequest.setUserId(userId.toString());
        }

        GoodOrder goodOrder = new GoodOrder();
        goodOrder.setUserId(Integer.valueOf(userRequest.getUserId()));
        goodOrder.setOrderNum(userRequest.getOrderNum());
        goodOrder.setStatus("1");
        List<Map> result = this.goodOrderService.selectByGoodOrderDetail(goodOrder);
        JSONObject returnJson = new JSONObject();
        if (result.size() > 0) {
            Map m  = result.get(0);
            JSONObject object = new JSONObject();
            String date = m.get("exchange_time").toString();
            object.put("exchangeTime", DateUtil.currentYYYYMMDDHHmmssWithSymbol(date));
            object.put("goodName", m.get("good_name"));
            object.put("goodImg", m.get("good_img"));
            object.put("goodType", m.get("good_type"));
            object.put("orderNum", m.get("order_num"));
            object.put("expressName", m.get("express_name"));
            object.put("score", m.get("good_down_virtual"));
            object.put("expressNum", m.get("express_num"));
            String status = m.get("exchange_status").toString();
            if (status.equals("1")) {
                object.put("exchangeStatus", "已下单");
            } else if (status.equals("2")) {
                object.put("exchangeStatus", "已发货");
            } else if (status.equals("3")) {
                object.put("exchangeStatus", "已完成");
            }
            object.put("addressDetail", m.get("user_address"));
            object.put("contact", m.get("contact"));
            object.put("phone", m.get("phone"));
            object.put("province_name", m.get("province_name"));
            object.put("city_name", m.get("city_name"));
            object.put("county_name", m.get("county_name"));
            returnJson.put("data", object);
        } else {
            returnJson.put("data", null);
        }

        return CallResult.success(returnJson);
    }
}
