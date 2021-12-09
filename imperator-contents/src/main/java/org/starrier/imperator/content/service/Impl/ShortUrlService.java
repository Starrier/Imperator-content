package org.starrier.imperator.content.service.Impl;

import static org.starrier.imperator.content.constant.Constant.DIGITS_BASE_64;

/**
 * @author starrier
 * @date 2020/11/22
 */
public class ShortUrlService {


    /**
     * 将十进制的数字转换为指定进制的字符串
     *
     * @param number 十进制的数字
     * @param seed   指定的进制
     * @return 指定进制的字符串
     */
    public static String toOtherNumberSystem(long number, int seed) {
        if (number < 0) {
            number = ((long) 2 * 0x7fffffff) + number + 2;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((number / seed) > 0) {
            buf[--charPos] = DIGITS_BASE_64[(int) (number % seed)];
            number /= seed;
        }
        buf[--charPos] = DIGITS_BASE_64[(int) (number % seed)];
        return new String(buf, charPos, (32 - charPos));
    }

    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字
     *
     * @param number 其它进制的数字（字符串形式）
     * @param seed   指定的进制，也就是参数str的原始进制
     * @return 十进制的数字
     */
    public static long toDecimalNumber(String number, int seed) {

        char[] charBuf = number.toCharArray();
        if (seed == 10) {
            return Long.parseLong(number);
        }

        long result = 0, base = 1;

        for (int i = charBuf.length - 1; i >= 0; i--) {
            int index = 0;
            for (int j = 0, length = DIGITS_BASE_64.length; j < length; j++) {
                //找到对应字符的下标，对应的下标才是具体的数值
                if (DIGITS_BASE_64[j] == charBuf[i]) {
                    index = j;
                }
            }
            result += index * base;
            base *= seed;
        }
        return result;
    }

    public String getShortUrlByNumberDigits() {

        return null;
    }


}
