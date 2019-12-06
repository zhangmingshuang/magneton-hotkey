package com.magneton.hotkey.server.demo.invoker;

import com.magneton.hotkey.server.trigger.rule.HourTrigggerRule;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
@HotkeyComponent("user")
public class UserTimeInvoker implements HotkeyInvoker<HourTrigggerRule> {

    @Override
    public HourTrigggerRule getRule() {
        return new HourTrigggerRule(1) {
            @Override
            public long getTime() {
                //由于是测试，所以修改10S
                return 10 * 1000;
            }
        };
    }

    @Override
    public void invoke(String key, HourTrigggerRule triggerRule) {
        System.out.println("UserTimeInvoker:" + key + "," + triggerRule);
        System.out.println("加载需要预热的数据。。。。。。");
    }
}
