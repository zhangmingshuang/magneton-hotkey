package com.magneton.hotkey.server.demo.invoker;

import com.magneton.hotkey.server.demo.invoker.HotkeyComponent;
import com.magneton.hotkey.server.demo.invoker.HotkeyInvoker;
import com.magneton.hotkey.server.trigger.rule.NumberTriggerRule;

/**
 * 该类表示的，监听热点数据key为test
 * 并且，被触发了 {@link NumberTriggerRule#getNumber()}次数之后
 * 回调{@link #invoke(String, NumberTriggerRule)}方法
 *
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@HotkeyComponent("test")
public class TestNumInvoker implements HotkeyInvoker<NumberTriggerRule> {

    @Override
    public NumberTriggerRule getRule() {
        //每被触发2次，回调
        return new NumberTriggerRule(2);
    }

    @Override
    public void invoke(String key, NumberTriggerRule triggerRule) {
        System.out.println("TestNumInvoker:" + key + "," + triggerRule);
        System.out.println("加载需要预热的数据。。。。。。");
    }
}
