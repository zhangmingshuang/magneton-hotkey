package com.magneton.hotkey.server.trigger;

import com.magneton.hotkey.server.trigger.rule.TriggerRule;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
public interface TriggerInvoker<T extends TriggerRule> {

    void invoke(String key, T triggerRule);
}
