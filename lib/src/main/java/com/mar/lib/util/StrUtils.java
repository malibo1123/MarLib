package com.mar.lib.util;

/**
 * Copyright (c) 2015 All rights reserved
 * 名称：StrUtils
 * 描述：字符串工具类
 *
 * @author cici
 * @date：2015/4/30 10:32
 */
public class StrUtils {
    /**
     * 描述：将null转化为“”.
     *
     * @param str 指定的字符串
     * @return 字符串的String类型
     */
    public static String parseEmpty(String str) {
        if(str==null || "null".equals(str.trim())){
            str = "";
        }
        return str.trim();
    }

    public static String encryptMobilePhone(String mobilePhone){
        if(mobilePhone.length()==11){
            char[] chars = mobilePhone.toCharArray();
            for (int i=3;i<9;i++){
                chars[i] = '*';
            }
            mobilePhone = String.valueOf(chars);
        }
        return mobilePhone;
    }


    /**
     * 描述：判断一个字符串是否为null或空值.
     * 建议使用下面的isStrNull增加大写NULL判空的情况
     * @param str 指定的字符串
     * @return true or false
     */
    @Deprecated
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || "null".equals(str);
    }

    /**
     * 转换16进制字符串为字节数组
     *
     * @param src 16进制字符串
     * @return 字节数据
     */
    public static byte[] str2bytes(String src) {

        if (src == null || src.length() == 0 || src.length() % 2 != 0) {
            return null;
        }
        int nSrcLen = src.length();
        byte byteArrayResult[] = new byte[nSrcLen / 2];
        StringBuffer strBufTemp = new StringBuffer(src);
        String strTemp;
        int i = 0;
        while (i<strBufTemp.length()-1)
        {
            strTemp = src.substring(i, i+2);
            byteArrayResult[i/2] = (byte) NumberUtils.parseInt(strTemp, 16);
            i+=2;
        }
        return byteArrayResult;
    }

    /**
     * 转换字节数组为16进制字符串
     *
     * @return 16进制字符串
     */
    public static String byteArrayToHexString(byte[] bytes) {
        if (bytes == null)
        {
            return "";
        }
        StringBuffer buff = new StringBuffer();
        int len = bytes.length;
        for (int j = 0; j < len; j++)
        {
            if ((bytes[j] & 0xff) < 16)
            {
                buff.append('0');
            }
            buff.append(Integer.toHexString(bytes[j] & 0xff));
        }
        return buff.toString().toUpperCase();
    }

    /**
     * 小数点格式话
     * @param s
     * @return
     */
    public static String strPatternN(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 下载次数格式化
     */
    public static String formatDownCount(String countStr) {
        double count = NumberUtils.parseDouble(countStr);
        if (count >= 10000) {
            double d = count / 10000d;

            if(count % 10000 ==0) {
                return String.format("%.0f", d) + "万";
            }

            return String.format("%.1f", d) + "万";
        }else {
            return countStr;
        }
    }

    /**
     * 描述：判断一个字符串是否为null或空值.
     * 由于和服务器交互返回的数据经常出现null
     * 或者NULL情况因此增加此判断函数
     * add by malibo 2017.09.21
     */
    public static boolean isStrNull(String str) {
        return str == null || "null".equals(str.toLowerCase()) ||
                "".equals(str.toLowerCase()) || str.trim().length() == 0 ||
                "null".equals(str.trim().toLowerCase());//之所以使用这个顺序是因为trim会新建字符串效率比较低，而且前三种覆盖了大部分情况
    }
}
