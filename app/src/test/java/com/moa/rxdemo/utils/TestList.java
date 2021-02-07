package com.moa.rxdemo.utils;

import com.moa.baselib.utils.GsonHelper;
import com.moa.baselib.utils.Hz2Pinying;
import com.moa.baselib.utils.Randoms;
import com.moa.rxdemo.mvp.bean.WebSocketBean;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestList {

    @Test
    public void testRemove() {
        List<String> strings = new ArrayList<>();
        strings.add("11111");
        strings.add("11112");
        strings.add("11113");
        strings.add("11114");
        System.out.println(Arrays.toString(strings.toArray()));
//        for (String s : strings){
//            strings.remove(s);
//        }

        // 动态删除集合中的数据 防止出现数据越界
        // 使用动态获取size()函数，strings.size()
//        for (int i = 0; i < strings.size() -1; i++) {
//            strings.remove(i);
//        }

        // 使用迭代器 遍历会动态更新index
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            iterator.remove();
        }

        System.out.println(Arrays.toString(strings.toArray()));
    }

    @Test
    public void testCountDownLatch() {
        System.out.println(getResult());
    }

    private String getResult() {
        int size = 5;
        // 阻塞线程等待所有线程执行完
        final CountDownLatch countDownLatch = new CountDownLatch(size);

        final String[] result = {""};

        for (int i = 0; i < size; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result[0] = result[0] + finalI + ", ";
                    countDownLatch.countDown();
                    System.out.println(result[0]);
                }
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result[0];
    }

    @Test
    public void testRandom() {

        for (int i = 0; i < 20; i++) {
            System.out.print(Randoms.randomInt()+" ");
        }
    }

    @Test
    public void hanzi2Pinyin() {
        String hanzi = "额";
        System.out.println(Hz2Pinying.oneHz2PyUpFirst(hanzi));

    }

    @Test
    public void testSplit() {
        String hanzi = "454555454554,";
        System.out.println(hanzi.split(",")[0]);

        BigDecimal bigDecimal1 = new BigDecimal("0.01");
        BigDecimal bigDecimal2 = new BigDecimal("2.1");
        bigDecimal1 = bigDecimal1.add(bigDecimal2);
        System.out.println(bigDecimal1.toString());

    }


    @Test
    public void regexTest() {
        String regex = "<img[^>]*>";
        String str = "xxxx";
        str = str.replaceAll(regex, "");
        System.out.println();

    }

    @Test
    public void gsonTest() {
        WebSocketBean bean = GsonHelper.toObj("{\"code\":3,\"msg\":\"tttttttttttttttttt\"}", WebSocketBean.class);

    }


}
