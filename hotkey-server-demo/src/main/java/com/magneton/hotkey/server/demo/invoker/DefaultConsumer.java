package com.magneton.hotkey.server.demo.invoker;

import com.magneton.hotkey.server.demo.invoker.HotkeyComponent;
import com.magneton.hotkey.server.demo.invoker.HotkeyInvoker;
import com.magneton.hotkey.server.trigger.rule.NumberTriggerRule;
import com.magneton.hotkey.server.trigger.rule.TriggerRule;

/**
 * 默认的处理器
 * 处理非配置的所有回调
 *
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@HotkeyComponent("*")
public class DefaultConsumer implements HotkeyInvoker<NumberTriggerRule> {

    @Override
    public NumberTriggerRule getRule() {
        return new NumberTriggerRule(10);
    }

    @Override
    public void invoke(String key, NumberTriggerRule triggerRule) {
        System.out.println("DefaultConsumer:" + key + "," + triggerRule);
        System.out.println("加载需要预热的数据。。。。。。");
    }
}
