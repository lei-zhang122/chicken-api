package com.chicken.api.util;

/**
 * @program:
 * @description: 常量工具类
 * @author: zhanglei
 * @create: 2018-07-25 10:39
 **/
public class ContantUtil {

    public static Integer DEFAULT_PAGE_NUM = 1;

    public static Integer DEFAULT_PAGE_SIZE = 20;

    //是否登录
    public static String IS_LOGIN = "log:sid:";

    //连续打卡天数
    public static String TOTAL_KEY = "total:id:";

    //用户一共打卡天数
    public static String USER_TOTAL_KEY = "user:total:id:";

    //打卡
    public static String SIGNED_KEY = "signed:id:";

    //每天最多获得积分
    public static String MAX_SOCRE_DAY = "d:max:score:day";

    //用户已经获得积分
    public static String GAIN_SCORE = "gain:score:";

    //用户打某一个用户今日获得分值
    public static String HIT_USER_SCORE_TODAY = "uo:hit:uo:s:";

    //每天打某一用户最多获得积分
    public static String MAX_HIT_USER = "d:hit:user:s";

    //用户排行榜
    public static String USER_RANKING_LIST = "userRankList";

    //好友排行榜 friendRankList:openid
    public static String FRIEND_RANKING_LIST = "friendRankList:";

    //属于哪个好友排行榜 user:owner:set:openid
    public static String USER_OWNER_SET = "user:owner:set:";

    //用户信息
    public static String USER_INFO = "user:id:";

    //用户OPENID
    public static String OPEN_ID = "openid:";

    //用户可一天邀请几次
    public static String USER_INVITE_ONE_DAY = "d:invite:count:day";

    //用户邀请的分
    public static String INVITE_SCORE = "d:invite:score";

    //用户已经邀请次数
    public static String USER_INVITE_COUNT = "user:invite:count:";

    //微信access_token
    public static String ACCESS_TOKEN = "access_token";

    //fromid
    public static String FROMID_INFO = "fromid:list:";

    //订单号
    public static String ORDER_NUM = "order:num";

    //温馨提示
    public static String TIPS_CONTENT = "d:tips:content";

    //暴击概率
    public static String HIT_PROBABILY="d:hit:probability";

    //命中大力丸的概率
    public static String GAIN_PROBABILY="d:gain:probability";

    //分值list
    public static String SCORE_LIST="d:scorelist";

    //兑换成功
    public static String EXCHANGE_SUCCESS_TEMPLATE="sET2SSUEohQezhLEzQjtw_cP1RqiT7YG3rRCZpuCx24";

    //发货通知
    public static String SEND_EXPRESS_TEMPLATE="WDnBh9PPYp-mXT3zxjG1jBP8zsnuAu-eHb3MnO3k2xI";

    //用户黑名单
    public static String BLACK_USER="b:u:";
}
