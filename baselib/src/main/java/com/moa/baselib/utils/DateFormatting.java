package com.moa.baselib.utils;

import android.content.Context;

import com.moa.baselib.BaseApp;
import com.moa.baselib.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 格式化时间
 *
 * @author wangjian
 * @create 2018/12/18
 */
public class DateFormatting {

    private static ThreadLocal<SimpleDateFormat> FULL_FORMATTER = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> YEAR_MONTH_DAY_H_M_FORMATTER = new ThreadLocal<>();
    private static ThreadLocal<DateFormat> TIME_FORMATTER = new ThreadLocal<>();
    private static ThreadLocal<DateFormat> H_M_FORMATTER = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> DATE_YEAR_FORMATTER = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> DATE_FORMATTER = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> DATE_FORMATTER_DOT = new ThreadLocal<>();
    private static ThreadLocal<SimpleDateFormat> MONTH_FORMATTER = new ThreadLocal<>();
    private static ThreadLocal<Calendar> CALENDAR = new ThreadLocal<>();

    private static Locale mLocale = Locale.getDefault();

    /**
     * 全格式化样式如：2018-12-18 11:11:41
     *
     * @param time 毫秒值
     * @return 格式化后的时间
     */
    public static String formatFull(long time) {
        return getFullFormatter().format(new Date(time));
    }

    /**
     * 格式化样式如：2018-12-18 11:11
     *
     * @param time 毫秒值
     * @return 格式化后的时间
     */
    public static String formatYMDHM(long time) {
        return getYMDHMFormatter().format(new Date(time));
    }

    /**
     * 格式化时间如：2018-12-18
     *
     * @param time 时间毫秒值
     * @return 格式化后的时间
     */
    public static String formatDate(long time) {
        return getDateFormatter().format(time);
    }

    /**
     * 格式化时间如：2018.12.18
     *
     * @param time 时间毫秒值
     * @return 格式化后的时间
     */
    public static String formatDateDot(long time) {
        return getDateDotFormatter().format(time);
    }

    /**
     * 格式化时间如：11:11:41 时：分：秒
     *
     * @param time 时间毫秒值
     * @return 格式化后的时间
     */
    public static String formatTime(long time) {
        return getTimeFormatter().format(new Date(time));
    }

    /**
     * 格式化时间如：11:11 时：分
     *
     * @param time 时间毫秒值
     * @return 格式化后的时间
     */
    public static String formatHMTime(long time) {
        return getHMFormatter().format(new Date(time));
    }

    /**
     * 格式化时间如：2012
     *
     * @param time 时间毫秒值
     * @return 格式化后的时间
     */
    public static String formatYear(long time) {
        return getDateYearFormatter().format(new Date(time));
    }

    /**
     * 2个时间是否同一天
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean areSameDays(long a, long b) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(a);
        int y1 = calendar.get(Calendar.YEAR);
        int m1 = calendar.get(Calendar.MONTH);
        int d1 = calendar.get(Calendar.DATE);
        calendar.setTimeInMillis(b);
        int y2 = calendar.get(Calendar.YEAR);
        int m2 = calendar.get(Calendar.MONTH);
        int d2 = calendar.get(Calendar.DATE);

        return y1 == y2 && m1 == m2 && d1 == d2;
    }

    /**
     * 格式化当前显示的时间
     *
     * @param context
     * @param time
     * @return
     */
    public static String formatShowDate(Context context, long time) {
        // 年月日时分秒
        String ymdHM = context.getString(R.string.tt_date_format_y_m_d_H_M);
        // 月日
        String MONTH_AND_DAY = context.getString(R.string.tt_date_format_m_d);
        // 时分
        String HOUR_AND_MINUTE = context.getString(R.string.tt_date_format_H_M);

        if (time == Long.MAX_VALUE) {
            return context.getString(R.string.tt_date_format_y_m_d_H_M);
        }
        SimpleDateFormat sf = null;
        long differTime = new Date().getTime() - time;

        long d = differTime / (1000 * 60 * 60 * 24);// 天
        if (d <= -2) {// 具体时间
            sf = new SimpleDateFormat(ymdHM, mLocale);
            return sf.format(time);
        } else if (d == -1) {// 明天
            // sf = new SimpleDateFormat(ymdHM);
            return context.getString(R.string.tt_date_format_torr);
        } else if (d == 0) {// 今天
            sf = new SimpleDateFormat(HOUR_AND_MINUTE, mLocale);
            return sf.format(new Date(time));
        } else if (d == 1) {// 昨天
            return context.getString(R.string.tt_date_format_yest);
        } else {// 具体时间
            sf = new SimpleDateFormat(MONTH_AND_DAY, mLocale);
            return sf.format(time);
        }
    }

    private static DateFormat getTimeFormatter() {
        DateFormat dateFormat = TIME_FORMATTER.get();
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("HH:mm:ss", mLocale);
            TIME_FORMATTER.set(dateFormat);
        }
        return dateFormat;
    }

    private static DateFormat getHMFormatter() {
        DateFormat dateFormat = H_M_FORMATTER.get();
        if (dateFormat == null) {
            dateFormat = new SimpleDateFormat("HH:mm", mLocale);
            H_M_FORMATTER.set(dateFormat);
        }
        return dateFormat;
    }

    private static Calendar getCalendar() {
        Calendar calendar = CALENDAR.get();
        if (calendar == null) {
            calendar = Calendar.getInstance();
            CALENDAR.set(calendar);
        }
        return calendar;
    }

    private static SimpleDateFormat getFullFormatter() {
        SimpleDateFormat res = FULL_FORMATTER.get();
        if (res == null) {
            res = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", mLocale);
            FULL_FORMATTER.set(res);
        }
        return res;
    }

    private static SimpleDateFormat getYMDHMFormatter() {
        SimpleDateFormat res = YEAR_MONTH_DAY_H_M_FORMATTER.get();
        if (res == null) {
            res = new SimpleDateFormat("yyyy-MM-dd HH:mm", mLocale);
            YEAR_MONTH_DAY_H_M_FORMATTER.set(res);
        }
        return res;
    }


    private static SimpleDateFormat getDateYearFormatter() {
        SimpleDateFormat res = DATE_YEAR_FORMATTER.get();
        if (res == null) {
            res = new SimpleDateFormat("yyyy", mLocale);
            DATE_YEAR_FORMATTER.set(res);
        }
        return res;
    }

    private static SimpleDateFormat getMonthFormatter() {
        SimpleDateFormat res = MONTH_FORMATTER.get();
        if (res == null) {
            res = new SimpleDateFormat("MMMM", mLocale);
            MONTH_FORMATTER.set(res);
        }
        return res;
    }

    private static SimpleDateFormat getDateDotFormatter() {
        SimpleDateFormat res = DATE_FORMATTER_DOT.get();
        if (res == null) {
            res = new SimpleDateFormat("yyyy.MM.dd", mLocale);
            DATE_FORMATTER_DOT.set(res);
        }
        return res;
    }

    private static SimpleDateFormat getDateFormatter() {
        SimpleDateFormat res = DATE_FORMATTER.get();
        if (res == null) {
            res = new SimpleDateFormat("yyyy-MM-dd", mLocale);
            DATE_FORMATTER.set(res);
        }
        return res;
    }

    public static String getTimeString(int time, boolean always2Num) {
        return getTimeString(time, always2Num, ":", ":", "");
    }

    /**
     * @param time
     * @return
     */
    public static String getTimeString(int time) {
        Context context = BaseApp.getAppContext();
        String hDivider = context.getString(R.string.tt_date_format_H);
        String mDivider = context.getString(R.string.tt_date_format_m);
        String sDivider = context.getString(R.string.tt_date_format_s);
        return getTimeString(time, true, hDivider, mDivider, sDivider);
    }


    /**
     * 把时间转换为：时分秒格式。
     *
     * @param time       传入时间单位为秒
     * @param always2Num 是否一直显示2位有效数字 如 true : 11:01:09, false : 11:1:9
     * @param hDivider   小时后面的分隔符
     * @param mDivider   分钟后面的分隔符
     * @param sDivider   秒后面的分隔符
     * @return 1时:2分:3秒 或 1:2:3
     */
    public static String getTimeString(int time, boolean always2Num, String hDivider, String mDivider, String sDivider) {
        int miao = time % 60;
        int fen = time / 60;
        int hour = 0;
        if (fen >= 60) {
            hour = fen / 60;
            fen = fen % 60;
        }
        String timeString = "";
        String miaoString = "";
        String fenString = "";
        String hourString = "";
        if (always2Num && miao < 10) {
            miaoString = "0" + miao;
        } else {
            miaoString = miao + "";
        }
        if (always2Num && fen < 10) {
            fenString = "0" + fen;
        } else {
            fenString = fen + "";
        }
        if (always2Num && hour < 10) {
            hourString = "0" + hour;
        } else {
            hourString = hour + "";
        }
        if (hour != 0) {
            timeString = hourString + hDivider + fenString + mDivider + miaoString + sDivider;
        } else {
            if (fen != 0) {
                timeString = fenString + mDivider + miaoString + sDivider;
            } else {
                // 只有秒时，秒后面默认带一个s
                timeString = miaoString + ((sDivider == null || sDivider.length() == 0) ? "s" : sDivider);
            }
        }
        return timeString;
    }
}