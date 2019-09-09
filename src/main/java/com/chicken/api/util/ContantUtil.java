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

    public static String DEFAULT_USER_NAME = "user_session";

    public static String DEFAULT_LOGIN_CODE = "login_code";

    public static String NO_LOGIN_MESSAGE = "请登陆之后，再操作。";


    //连续打卡天数
    public static String TOTAL_KEY = "total:id:";

    //打卡
    public static String SIGNED_KEY = "signed:id:";

    //每天最多获得积分
    public static String MAX_SOCRE_DAY="max:score:day";

    //用户已经获得积分
    public static String GAIN_SCORE="gain:score:";

    //用户排行榜
    public static String USER_LIST="userInfoList";

    //用户信息
    public static String USER_INFO="user:id:";
}
