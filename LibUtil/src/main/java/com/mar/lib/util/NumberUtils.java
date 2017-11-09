package com.mar.lib.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/15.
 */
public class NumberUtils {

    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    /**
     * 支持小数点，向上取整
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static int parseInt(String str, int defaultValue) {
        int number = defaultValue;
        if (!TextUtils.isEmpty(str)) {
            try {
                Double d = Double.parseDouble(str);
                d = Math.ceil(d);
                number = d.intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return number;
    }

    public static float parseFloat(String str) {
        float number = 0;
        if (!TextUtils.isEmpty(str)) {
            try {
                number = Float.parseFloat(str);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return number;

    }

    public static double parseDouble(String str) {
        double number = 0;
        if (!TextUtils.isEmpty(str)) {
            try {
                number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return number;
    }


    /**
     * 支持小数点，向上取整
     *
     * @param str
     * @return
     */
    public static long parseLong(String str) {
        long number = 0;
        if (!TextUtils.isEmpty(str)) {
            try {
                Double d = Double.parseDouble(str);
                d = Math.ceil(d);
                number = d.longValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return number;
    }

    /**
     * 末尾为0省略0,未四舍五入
     *
     * @param s
     * @return
     */
    public static String getDoubleStrWithOneDecimal(String s) {
        try {
            if (s.indexOf(".") != -1) {
                s = s.substring(0, s.indexOf(".") + 2);
                if (s.charAt(s.length() - 1) == '0') {
                    s = s.substring(0, s.indexOf("."));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getDistance(String s_distance) {
        long distance = parseLong(s_distance);
        if (distance < 1000) {
            return "1km";
        } else if (distance < 10000 * 1000) {
            String s = new BigDecimal(String.valueOf(distance)).divide(new BigDecimal(String.valueOf(1000)), 2, BigDecimal.ROUND_HALF_UP).toString();
            return subZeroAndDot(s) + "km";
        } else {
            String s = new BigDecimal(String.valueOf(distance)).divide(new BigDecimal(String.valueOf(10000 * 1000)), 2, BigDecimal.ROUND_HALF_UP).toString();
            return subZeroAndDot(s) + "万km";
        }
    }

    public static String getYuWanCount(String dw) {
        long wight = parseLong(dw);
        if (wight < 1000) {
            return wight + "g";
        } else if (wight < 1000 * 1000) {
            String s = new BigDecimal(String.valueOf(dw)).divide(new BigDecimal(String.valueOf(1000)), 3, BigDecimal.ROUND_FLOOR).toString();
            return subZeroAndDot(s) + "kg";
        } else {
            String s = new BigDecimal(String.valueOf(dw)).divide(new BigDecimal(String.valueOf(1000 * 1000)), 2, BigDecimal.ROUND_FLOOR).toString();
            return subZeroAndDot(s) + "t";
        }
    }

    /**
     * 千位表示法
     *
     * @param num
     * @return
     */
    public static String numberWithDelimiter(long num) {
        StringBuilder numStr = new StringBuilder();
        int len = numStr.append(num).length();
        if (len <= 3) return numStr.toString();   //如果长度小于等于3不做处理
        while ((len -= 3) > 0) { //从个位开始倒序插入
            numStr.insert(len, ",");
        }
        return numStr.toString();
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    public static boolean isHit(int probability) {

        return new Random().nextInt(100) < probability;
    }


    public static String formatNum(int i) {

        if (i < 0) {
            return "00";
        }

        switch (i) {
            case 0:
                return "00";
            case 1:
                return "01";
            case 2:
                return "02";
            case 3:
                return "03";
            case 4:
                return "04";
            case 5:
                return "05";
            case 6:
                return "06";
            case 7:
                return "07";
            case 8:
                return "08";
            case 9:
                return "09";
            default:
                return "" + i;
        }
    }

    /**
     * @return 获得区间内的一个随机数
     */
    public static int randomInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * 格式化times 超过一万以万为单位 保留一位小数 不超过一万返回原计数
     *
     * @param times
     * @return
     */
    public static String formateTimes(long times) {

        if (times < 10000) {

            return String.valueOf(times);
        } else {

            double d = (double) (times) / (double) 10000;
            return String.format("%2.1f", d) + "万";
        }
    }

    /**
     * 格式化关注数量
     *
     * @param followNum
     * @return
     */
    public static String formatFollowNum(long followNum) {

        if (followNum > 100000) {
            double d = (double) followNum / (double) 10000;
            return String.valueOf(d).substring(0, String.valueOf(d).indexOf(".") + 2) + "万";
            //follow_count_txt.setText(String.format("%2.1f", d) + "万");
        } else {
            return String.valueOf(followNum);
        }
    }

    /**
     * 金额转化为120,000
     *
     * @param data
     * @return
     */
    public static String formatTosepara(String data) {

        double amount = NumberUtils.parseDouble(data);
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(amount);
    }

    /**
     * float保留4位小数
     *
     * @param number
     * @return
     */
    public static float get4bitFloat(float number) {
        DecimalFormat df = new DecimalFormat("#.####");
        return Float.valueOf(df.format(number));
    }

    /**
     * 已万未单位，四舍五入
     *
     * @param number
     * @return
     */
    public static String tenThousands(int number) {
        if (number > 10000) {
            String s = new BigDecimal(String.valueOf(number)).divide(new BigDecimal(String.valueOf(10000)), 1, BigDecimal.ROUND_HALF_UP).toString();
            return subZeroAndDot(s) + "万";
        } else {
            return String.valueOf(number);
        }
    }

    /**
     * 已万未单位，无四舍五入(主播等级经验值显示法)
     *
     * @param number
     * @return
     */
    public static String newTenThousands(double number, int i) {
        DecimalFormat formater = new DecimalFormat();
        formater.setMaximumFractionDigits(i);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        if (number > 10000) {
            String value = formater.format((number / 10000));
            return value + "万";
        } else {
            String value = formater.format(number);
            return value;
        }
    }


    /**
     * 判断字符串是否全为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 超过最大值显示 num++
     *
     * @param number
     * @return
     */
    public static String showNumMaxJJ(int number, int max) {

        if (max <= 0 || number <= max) {
            return number + "";
        }

        return max + "+";
    }

    /**
     * 对传入参数除100,默认整除时保留小数位
     *
     * @param num
     * @param decNum 保留的小数位
     * @param decNum 末位为.0...时是否保留小数位
     * @return
     */
    public static String divideHundred(long num, int decNum, boolean retainDec) {
        BigDecimal result = new BigDecimal(num).divide(new BigDecimal(100)).setScale(decNum >= 0 ? decNum : 0, BigDecimal.ROUND_FLOOR);
        if (!retainDec) {
            return getDoubleStrWithOneDecimal(result.toString());
        } else {
            return result.toString();
        }
    }

    /**
     * 精确的 乘法运算
     *
     * @param var1 double 乘数1
     * @param var2 double 乘数2
     * @return
     */
    public static double mul(double var1, double var2) {

        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
        return bigDecimal1.multiply(bigDecimal2).doubleValue();
    }

    /**
     * 精确的 加运算
     *
     * @param var1 参数1
     * @param var2 参数1
     * @return
     */
    public static double add(double var1, double var2) {

        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
        return bigDecimal1.add(bigDecimal2).doubleValue();
    }

    /**
     * 精确的 减法运算
     *
     * @param var1 参数1
     * @param var2 参数2
     * @return
     */
    public static double sub(double var1, double var2) {

        BigDecimal bigDecimal1 = new BigDecimal(Double.toString(var1));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(var2));
        return bigDecimal1.subtract(bigDecimal2).doubleValue();
    }
}
