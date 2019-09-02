package com.chicken.api.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);



    /**
     * 根据给定的日期，返回给定的字符串， 返回 字符串的形式是：yyyy-MM-dd HH:mm:ss
     *
     * @param date 要格式化的日期
     * @return 将日期格式化后返回的字符串，以这中格式返回：yyyy-MM-dd HH:mm:ss
     */
    public static String getStrByDate(Date date, String format) {
        assert date != null && format != null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 获得当前系统时间，格式为yyyyMMdd
     *
     * @return 格式化后的时间
     */
    public static String currentYYYYMMDD() {
        return getStrByDate(new Date(), "yyyyMMdd");
    }

    /**
     * 获得当前系统时间，格式为HHmmss
     *
     * @return 格式化后的时间
     */
    public static String currentHHMMSS() {
        return getStrByDate(new Date(), "HHmmss");
    }

    /**
     * 获得当前系统时间，格式为yyyyMMddHHmmss
     *
     * @return 格式化后的时间
     */
    public static String currentYYYYMMDDHHmmss() {
        return getStrByDate(new Date(), "yyyyMMddHHmmss");
    }

    /**
     * 获得当前系统时间，格式为yyyy-MM-dd
     *
     * @return 格式化后的时间
     */
    public static String currentYYYYMMDDWithSymbol() {
        return getStrByDate(new Date(), "yyyy-MM-dd");
    }

    /**
     * 获得当前系统时间，格式为HH:mm:ss
     *
     * @return 格式化后的时间
     */
    public static String currentHHMMSSWithSymbol() {
        return getStrByDate(new Date(), "HH:mm:ss");
    }

    /**
     * 获得当前系统时间，格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 格式化后的时间
     */
    public static String currentYYYYMMDDHHmmssWithSymbol() {
        return getStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

     /**
     * 获得当前系统时间，格式为yyyy-MM-dd
     *
     * @return 格式化后的时间
     */
    public static String currentYYYYMMDDWithSymbol(Date date) {
        return getStrByDate(date, "yyyy-MM-dd");
    }

    /**
     * 获得当前系统时间，格式为HH:mm:ss
     *
     * @return 格式化后的时间
     */
    public static String currentHHMMSSWithSymbol(Date date) {
        return getStrByDate(date, "HH:mm:ss");
    }

    /**
     * 获得当前系统时间，格式为yyyy-MM-dd HH:mm:ss
     *
     * @return 格式化后的时间
     */
    public static String currentYYYYMMDDHHmmssWithSymbol(Date date) {
        return getStrByDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获得指定日期的前一天 yyyy-MM-dd
     * @param date
     * @return
     */
    public static String getSpecifiedDayBefore(Date date, String dateFormat){
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day=c.get(Calendar.DATE);
        c.set(Calendar.DATE,day-1);

        String dayBefore=new SimpleDateFormat(dateFormat).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getSpecifiedDayAfter(Date date, String dateFormat) {
        if (date == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        String dayAfter = new SimpleDateFormat(dateFormat).format(c.getTime());
        return dayAfter;
    }

    /**
     * 将时间戳转换为字符串格式的日期
     * 例如：输入1504585491128；输出为2017-09-05 12:24:51
     * @param timeStamp
     * @return
     */
    public static String parseTimeStamp2DateString(long timeStamp) {
        SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String result;
        try {
            result = format.format(timeStamp);
        } catch (Exception e) {
            return "" ;
        }

        return result;
    }

    public static Date parse(String dateString, String formatString){
        if(StringUtils.isEmpty(dateString)){
            return null;
        }
        if (StringUtils.isEmpty(formatString)){
            formatString = "yyyy-MM-dd";
        }
        return parseByPattern(dateString, formatString);
    }

    private static Date parseByPattern(String dateString, String pattern){
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * 获取当月第一天
     * @return
     */
    public static String firstDay(){

        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday;
        // 获取前月的第一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        return firstday;
    }

    /**
     * 获取当月最后一天
     * @return
     */
    public static String lastDay(){
        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String lastday;
        // 获取前月的最后一天
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        return  lastday;
    }

    /**
     * 获取前一小时
     * @return
     */
    public static Date getFormerHour() throws Exception{
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cale = null;
        cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);
        int hour = cale.get(Calendar.HOUR_OF_DAY)-1;
        return format.parse(year+"-"+month+"-"+day+" "+hour+":00:00");
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getNow(){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
            return format.format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

}
