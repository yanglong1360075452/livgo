package com.wizinno.livgo.app.utils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/9.
 */
public class WeekTime {
    public static Date getPastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        Date today = calendar.getTime();
        return today;
    }
}
