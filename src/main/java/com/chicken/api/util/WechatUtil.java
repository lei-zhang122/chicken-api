package com.chicken.api.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglei
 * @date 2019-09-11 16:49
 */
public class WechatUtil {

    //获取access_token
    private final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&";
    //推送url
    private final static String PUSH_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";

    private final static String APP_ID = "wx3947585d87bc8135";

    private final static String SECRET = "7a49f8e8a62cec0d903434996ad4e78e";

    /**
     * 获得openid
     * @param code
     * @return
     */
    public static JSONObject getSessionKeyOrOpenId(String code) {
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> requestUrlParam = new HashMap<>();
        // https://mp.weixin.qq.com/wxopen/devprofile?action=get_profile&token=164113089&lang=zh_CN
        //小程序appId
        requestUrlParam.put("appid", APP_ID);
        //小程序secret
        requestUrlParam.put("secret", SECRET);
        //小程序端返回的code
        requestUrlParam.put("js_code", code);
        //默认参数
        requestUrlParam.put("grant_type", "authorization_code");
        //发送post请求读取调用微信接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpClientUtil.doPost(requestUrl, requestUrlParam));
        return jsonObject;
    }


    /**
     * 获得accessToken
     * @return
     */
    public static JSONObject getAccessToken() {

        String url = ACCESS_TOKEN_URL + "appid=" + APP_ID + "&secret=" + SECRET;
        PrintWriter out = null;
        BufferedReader in = null;
        String line;
        StringBuffer sb = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();

            // 设置通用的请求属性 设置请求格式
            //设置返回类型
            conn.setRequestProperty("contentType", "text/plain");
            //设置请求类型
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            //设置超时时间
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.setDoOutput(true);
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应    设置接收格式
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            // 将获得的String对象转为JSON格式
            JSONObject jsonObject = JSONObject.parseObject(sb.toString());
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 推送工具类
     * @author zhanglei
     * @date 2018/12/25
     * @param params 推送消息内容
     * @param accessToken
     * @return boolean
     */
    public static boolean setPush(String params, String accessToken) {
        boolean flag = false;
        String url = PUSH_URL + accessToken;
        OutputStream outputStream = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        HttpsURLConnection connection = null;
        try {
            // 创建URL对象
            URL realUrl = new URL(url);
            // 打开连接 获取连接对象
            connection = (HttpsURLConnection) realUrl.openConnection();
            // 设置请求编码
            connection.addRequestProperty("encoding", "UTF-8");
            // 设置允许输入
            connection.setDoInput(true);
            // 设置允许输出
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != params) {
                outputStream = connection.getOutputStream();
                // 注意编码格式
                outputStream.write(params.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            JSONObject jsonObject = JSONObject.parseObject(buffer.toString());
            int errorCode = jsonObject.getInteger("errcode");
            String errorMessage = jsonObject.getString("errmsg");
            if (errorCode == 0) {
                flag = true;
            } else {
                System.out.println("模板消息发送失败:" + errorCode + "," + errorMessage);
                flag = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 依次关闭打开的输入流
            try {
                connection.disconnect();
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
                // 依次关闭打开的输出流
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
}
