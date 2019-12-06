package com.magneton.hotkey.common;

import java.util.List;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public interface LoadBalance {

    <T> Invoker<T> doSelect(List<Invoker<T>> invokers);
}
