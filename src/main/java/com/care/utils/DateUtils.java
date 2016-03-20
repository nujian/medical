package com.care.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by nujian on 2015/5/28.
 */

public class DateUtils {
    public static int diffDays(Date d1, Date d2) {
        return Math.abs(Days.daysBetween(new DateTime(d1), new DateTime(d2)).getDays());
    }

    public static int diffDaysWithoutAbs(Date d1,Date d2){
        return Days.daysBetween(new DateTime(d1),new DateTime(d2)).getDays();
    }


    public static int diffYear(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        return Math.abs(c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR));
    }

    public static Date preDate(Date end, int beforeDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.add(Calendar.DATE, -beforeDays);
        return calendar.getTime();
    }


    /**
     * 今天星期几
     * @return
     */
    public static String getWeekDay(){
        return getWeekDayByDate(null);
    }

    /**
     * 根据日期查看当天是星期几
     * @param date
     * @return
     */
    public static String getWeekDayByDate(Date date){
        Integer weekDay = null;
        if(date == null){
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        weekDay = weekDay-1;
        return String.valueOf(weekDay);
    }



    /**
     * @param startDate
     * @param num
     * @return
     */
    public static Date addDate(Date startDate,Integer num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DATE,num);
        return calendar.getTime();
    }


    public static List<Date> preDates(Date until, int limit) {
        List<Date> list = new ArrayList<Date>();
        for (int i = 1; i <= limit; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(until);
            c.add(Calendar.DATE, -i);
            list.add(c.getTime());
        }
        return list;
    }


    public static List<Date> dateListInBetween(Date begin, Date end) {
        List<Date> list = new ArrayList<Date>();
        list.add(begin);
        for (int i = 1; i <= diffDays(begin, end); i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(begin);
            c.add(Calendar.DATE, i);
            list.add(c.getTime());
        }
        return list;
    }


    public static long getSecondTimeStamp(Date date) {
        return date.getTime() / 1000;
    }


    public static Date parse(String dateString, String pattern) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

            return dateFormat.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }


    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }


    public static String format2WeeHoursByDate(Date date){
        return format(date,"yyyy-MM-dd")+" 00:00:00";
    }


    public static String format2LastByDate(Date date){
        return format(date,"yyyy-MM-dd")+" 23:59:59";
    }


    public static long getDiff2NowMinutes(String date) {
        long diff = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            diff = (System.currentTimeMillis() - (sdf.parse(date).getTime())) / 60000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }


    public static long getDiff2NowMinutes(Date date) {
        return (System.currentTimeMillis() - (date.getTime())) / 60000;
    }

    public static long getDiff2NowSecond(String date) {
        long diff = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            diff = (System.currentTimeMillis() - (sdf.parse(date).getTime())) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }

    public static long dateString2long(String datetime) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(datetime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTimeInMillis();
    }


    public static Date dateString2Date(String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = sdf.parse(date);
        return d;
    }


    public static Long getTimeDifOfMillisecond(String dateStr) {
        Long time = null;
        return (time = (System.currentTimeMillis() - dateString2long(dateStr))) > 0 ? time : null;
    }

    public static Long getDifOfSecondFromCurrentTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return (dateString2long(dateStr) - System.currentTimeMillis()) / 1000;
    }


    /**
     * 根据日期获得当月第一天和最后一天
     * @param date
     * @return
     */
    public static Map<String, String> getFirstday_Lastday_Month(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00");
        day_first = str.toString();

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.add(Calendar.DATE, -1);
        String day_last = df.format(calendar.getTime());
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59");
        day_last = endStr.toString();

        Map<String, String> map = new HashMap<String, String>();
        map.put("first", day_first);
        map.put("last", day_last);
        return map;
    }



    /**
     * 当月第一天
     * @return
     */
    public static String getFirstDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00");
        return str.toString();

    }

    /**
     * 当月最后一天
     * @return
     */
    public static String getLastDay() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();
        String s = df.format(theDate);
        StringBuffer str = new StringBuffer().append(s).append(" 23:59");
        return str.toString();
    }

    /**
     * 某年的第一天
     * @param year
     * @return
     */
    public static Date getFirstDayOfYear(int year){
        DateTime time = new DateTime(year,1,1,0,0,0,0);
        return time.toDate();
    }

    /**
     * 某年的最后一天
     * @param year
     * @return
     */
    public static Date getLastDayOfYear(int year){
        DateTime time = new DateTime(year,12,31,23,59,59,0);
        return time.toDate();
    }




    public static void main(String[] args) {
//        System.out.println(diffDaysWithoutAbs(DateUtils.parse("2015-05-06", "yyyy-MM-dd"), new Date()));

//        System.out.println(preDate(new Date(),-1));

//        System.out.println(preDate(new Date(),-1));

//        System.out.println(format(new Date(),"yyyy-MM-dd"));

        System.out.println(getWeekDayByDate(null));
    }

}