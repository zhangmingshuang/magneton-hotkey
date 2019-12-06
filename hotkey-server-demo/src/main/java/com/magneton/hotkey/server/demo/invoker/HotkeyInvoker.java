package com.magneton.hotkey.server.demo.invoker;

import com.magneton.hotkey.server.trigger.TriggerInvoker;
import com.magneton.hotkey.server.trigger.rule.TriggerRule;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
public interface HotkeyInvoker<T extends TriggerRule> extends TriggerInvoker<T> {

    T getRule();
}
