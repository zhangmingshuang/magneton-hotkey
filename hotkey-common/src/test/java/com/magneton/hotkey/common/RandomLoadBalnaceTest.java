package com.magneton.hotkey.common;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class RandomLoadBalnaceTest {

    @Test
    public void test() {
        LoadBalance loadBalance = new RandomLoadBalnace();
        List<Invoker<Integer>> invokers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Invoker invoker = new Invoker();
            invoker.setWeight(i * 10);
            invoker.setData(i);
            invokers.add(invoker);
        }
        Invoker<Integer> invoker = loadBalance.doSelect(invokers);
        System.out.println(invoker);
    }
}
