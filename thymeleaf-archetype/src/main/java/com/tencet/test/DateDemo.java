package com.tencet.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by pijiang on 2019/7/10.
 */
public class DateDemo {

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GTM+8"));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取本周的首天
        c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK));
        Date date = c.getTime();
        System.out.println(sf.format(date));
    }
}