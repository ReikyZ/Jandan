package com.reikyz.jandan.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by clownqiang on 15/8/10.
 */
public class TimeUtils {
    static TimeZone timeZone = TimeZone.getTimeZone(TimeZone.getDefault().getID());

    /**
     * 获取当前系统时间 格式 1月1日 00：00
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 时间格式转换
     * 2015-12-16T17:04:42+0800
     */
    public static String currentUTCTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        df.setTimeZone(timeZone);
        return df.format(new Date());
    }

    /**
     * date转换格式 1970-01-01
     * formatTime
     *
     * @param date
     * @return
     */
    public static String formatTime(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    /**
     * 照片时间转换为ISO格式的时间
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String parseToISOTime(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
    }

    /**
     * String to time mark
     */

    public static String parseMark(String str) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(timeZone);
        return getChatTimeStamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str).getTime());
    }

    /**
     * Date转换位ISO格式时间
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static String getISOTimeFromDate(Date date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }

    /**
     * long转换位ISO格式
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static String getISOTimeFromLong(long time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(new Date(time));
    }

    /**
     * ISO格式转换位long
     *
     * @param isoTime
     * @return
     */
    public static long getLongFromTime(String isoTime) {
        StringBuffer sbDate = new StringBuffer();
        sbDate.append(isoTime);
        String newDate = sbDate.substring(0, 19).toString();
        String rDate = newDate.replace("T", " ");
        String nDate = rDate.replaceAll("-", "/");
        long epoch = 0;
        try {
            epoch = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(nDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return epoch;
    }

    /**
     * 获得距某时的 时分秒
     *
     * @param isoTime
     * @return
     */
    public static String getLeftHour(String isoTime) {
        long leftTime = getLongFromTime(isoTime) - System.currentTimeMillis();
        int leftHour = (int) (leftTime / 3600 / 1000);
        if (leftHour < 10 && leftHour >= 0) {
            return "0" + leftHour;
        }
        return String.valueOf(leftHour);
    }

    public static String getLeftMin(String isoTime) {
        long leftTime = getLongFromTime(isoTime) - System.currentTimeMillis();
        int leftMin = (int) ((leftTime / 1000) % 3600 / 60);
        if (leftMin < 10 && leftMin >= 0) {
            return "0" + leftMin;
        }
        return String.valueOf(leftMin);
    }

    public static String getLeftSec(String isoTime) {
        long leftTime = getLongFromTime(isoTime) - System.currentTimeMillis();
        int leftSec = (int) ((leftTime / 1000) % 60);
        if (leftSec < 10 && leftSec >= 0) {
            return "0" + leftSec;
        }
        return String.valueOf(leftSec);
    }

    public static String getCountDown(Long timeLong, Long duration) {
        Long overTime = timeLong + duration;
        Long leftTime = overTime - System.currentTimeMillis();
        if (leftTime < 0) return "已过期";
        StringBuilder sb = new StringBuilder();

        int leftDay = (int) (leftTime / 1000 / (3600 * 24));
        if (leftDay > 0) {
            sb.append(leftDay + "天 ");
        }
        int leftHour = (int) (leftTime / 3600 / 1000);
        if (leftHour < 10 && leftHour >= 0) {
            sb.append("0");
            sb.append(leftHour);
        } else {
            sb.append(leftHour);
        }
        sb.append(":");
        int leftMin = (int) ((leftTime / 1000) % 3600 / 60);
        if (leftMin < 10 && leftMin >= 0) {
            sb.append("0");
            sb.append(leftMin);
        } else {
            sb.append(leftMin);
        }
        sb.append(":");
        int leftSec = (int) ((leftTime / 1000) % 60);
        if (leftSec < 10 && leftSec >= 0) {
            sb.append("0");
            sb.append(leftSec);
        } else {
            sb.append(leftSec);
        }
        return sb.toString();
    }


    public static String getTimeFromDate(Date date) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(date);
    }

    public static String getShowTimeStamp(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static String getShowTimeStamp(Long timeLong) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timeLong));
    }

    public static String getCommentTimeStamp(Long timeLong) {
        if (System.currentTimeMillis() - timeLong < 3600 * 1000 * 24) {
            return new SimpleDateFormat("今天").format(new Date(timeLong));
        } else if (System.currentTimeMillis() - timeLong < 3600 * 1000 * 24) {
            return new SimpleDateFormat("昨天").format(new Date(timeLong));
        } else {
            return new SimpleDateFormat("yyyy/MM/dd").format(new Date(timeLong));
        }
    }

    public static String getChatTimeStamp(Long timeLong) {
        if (System.currentTimeMillis() - timeLong < 3600 * 1000 * 24) {
            return new SimpleDateFormat("HH:mm").format(new Date(timeLong));
//        } else if (System.currentTimeMillis() - timeLong < 3600 * 1000 * 24) {
//            return new SimpleDateFormat("昨天 HH:mm:ss").format(new Date(timeLong));
        } else {
            return new SimpleDateFormat("MM-dd HH:mm").format(new Date(timeLong));
        }
    }

    public static Long getLongFronDate(Date date) throws ParseException {
        return getLongFromTime(getISOTimeFromDate(date));
    }


    public static Date getDateFromTime(String time) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(time);
    }

    /**
     * 将iso格式的时间转换成显示的时间
     *
     * @param isoTime
     * @return
     */
    public static String getTimeFromIso(String isoTime) {
        long createTime = getLongFromTime(isoTime);
        long currentTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        String currentYearZeroTime = currentYear + "-01-01 00:00:00";
        Date currentYearDate;
        long currentYearTime = 0;
        try {
            currentYearDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(currentYearZeroTime);
            currentYearTime = currentYearDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long oneDay = 3600 * 24 * 1000;
        if (currentTime - createTime < 60 * 1000) {
            return "刚刚";
        } else if (createTime - getTodayZero() >= 0) {
            return "" + new SimpleDateFormat("HH:mm").format(new Date(createTime));
        } else if (createTime >= currentYearTime) {
            return new SimpleDateFormat("MM月dd日 HH:mm").format(new Date(createTime));
        } else {
            return new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date(createTime));
        }

    }

    /**
     * 时间mark
     *
     * @param isoTime
     * @return
     */
    public static String getPastTimeMark(String isoTime) {
        long createTime = getLongFromTime(isoTime);
        long currentTime = System.currentTimeMillis();

        long det = (currentTime - createTime) / 1000;
        if (det < 60) {
            return "刚刚";
        } else if (det >= 60 && det < 3600) {
            return det / (60) + "分钟前";
        } else if (det >= 3600 && det < 86400) {
            return det / (3600) + "个小时前";
        } else if (det >= 86400 && det < 2592000) {
            return det / (86400) + "天前";
        } else if (det >= 2592000 && det < 31536000) {
            return det / (2592000) + "个月前";
        } else {
            return "很久以前…";
        }
    }

    /**
     * 获取当天零点的毫秒数
     *
     * @return
     */
    public static long getTodayZero() {
        Date date = new Date();
        long l = 24 * 60 * 60 * 1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。)，理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime() % l) - 8 * 60 * 60 * 1000);
    }

    /**
     * 根据日期计算年龄
     *
     * @param birthday
     * @return
     * @throws ParseException
     */
    public static int getAgeByBirthday(String birthday) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = simpleDateFormat.parse(birthday);
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(birthDate);
        int yearBirth = calendar.get(Calendar.YEAR);
        int monthBirth = calendar.get(Calendar.MONTH);
        int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    public static String getPastTime(long timeCount) {
        StringBuilder sb = new StringBuilder();
        int up, down;
        timeCount /= 1000;
        if (timeCount < 3600) {
            up = (int) (timeCount / 60);
            if (String.valueOf(up).length() <= 1) {
                sb.append("0" + up);
            } else {
                sb.append(up);
            }
            sb.append(":");

            down = (int) (timeCount % 60);
            if (String.valueOf(down).length() <= 1) {
                sb.append("0" + down);
            } else {
                sb.append(down);
            }


        } else {
            up = (int) (timeCount / 3600);
            if (String.valueOf(up).length() <= 1) {
                sb.append("0" + up);
            } else {
                sb.append(up);
            }
            sb.append(":");

            down = (int) (timeCount % 3600) / 60;
            if (String.valueOf(down).length() <= 1) {
                sb.append("0" + down);
            } else {
                sb.append(down);
            }
        }


        return sb.toString();
    }
}
