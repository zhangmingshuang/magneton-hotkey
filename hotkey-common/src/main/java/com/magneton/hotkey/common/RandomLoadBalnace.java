package com.magneton.hotkey.common;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class RandomLoadBalnace implements LoadBalance {

    @Override
    public <T> Invoker<T> doSelect(List<Invoker<T>> invokers) {
        int size = invokers.size();
        boolean sameWeight = true;
        int[] weights = new int[size];
        int firstWeight = invokers.get(0).getWeight();
        weights[0] = firstWeight;
        int totalWeight = firstWeight;
        for (int i = 1, l = invokers.size(); i < l; i++) {
            Invoker invoker = invokers.get(i);
            int weight = invoker.getWeight();
            weights[i] = weight;
            totalWeight += weight;
            if (sameWeight && weight != firstWeight) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            for (int i = 0; i < size; i++) {
                offset -= weights[i];
                if (offset < 0) {
                    return invokers.get(i);
                }
            }
        }
        return invokers.get(ThreadLocalRandom.current().nextInt(size));
    }
}
