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

    //打卡
    public static String SIGNED_KEY = "signed:id:";

    //每天最多获得积分
    public static String MAX_SOCRE_DAY = "max:score:day";

    //用户已经获得积分
    public static String GAIN_SCORE = "gain:score:";

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
    public static String USER_INVITE_ONE_DAY = "invite:count:day";

    //用户邀请的分
    public static String INVITE_SCORE = "invite:score";

    //用户已经邀请次数
    public static String USER_INVITE_COUNT = "user:invite:count:";

    //微信access_token
    public static String ACCESS_TOKEN="access_token";

    //消息模板id
    public static String DINING_TEMPLATE="qKfOCgoTzPMg7rHJZiURzRbqWZm29CSTm9SPw_yWpNE";
}
