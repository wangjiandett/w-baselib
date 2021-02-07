package com.moa.baselib.utils;

import java.util.Random;

/**
 * 获取随机数
 *
 * @author wangjian
 */
public class Randoms {

    private static final Random RANDOM = new Random();

    /**
     * 获取随机long整数
     */
    public static long randomLong() {
        synchronized (RANDOM) {
            return Math.abs(RANDOM.nextLong());
        }
    }

    /**
     * 获取随机Int整数
     */
    public static int randomInt() {
        synchronized (RANDOM) {
            return Math.abs(RANDOM.nextInt());
        }
    }

    /**
     * 获取[0,range范围的随机数]，如[0,100]
     *
     * @param range 最大值
     * @return 随机数
     */
    public static int randomInt(int range) {
        synchronized (RANDOM) {
            return RANDOM.nextInt(range + 1);
        }
    }

    /**
     * 获取指定长度的byte数组
     *
     * @param length 长度
     * @return byte数组
     */
    public static byte[] generateSeed(int length) {
        synchronized (RANDOM) {
            byte[] res = new byte[length];
            RANDOM.nextBytes(res);
            return res;
        }
    }
}
