package com.magneton.hotkey.server.trigger;

import com.magneton.hotkey.server.ConfigableHotkeyListener;
import com.magneton.hotkey.server.storager.HotkeyStorager;
import com.magneton.hotkey.server.trigger.rule.TriggerRule;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public interface HotkeyTrigger extends ConfigableHotkeyListener {

    void registerStorager(HotkeyStorager hotkeyStorager);

    /**
     * 注册一个触发器
     *
     * 当监听器判断key满足触发规则{@code rule}时，
     * 会调用对应的{@link TriggerInvoker}
     *
     * @param key            热点key
     * @param rule           规则
     * @param triggerInvoker 规则触发器
     */
    <T extends TriggerRule> void registerInvoker(String key, T rule, TriggerInvoker<T> triggerInvoker);

    /**
     * 注册默认规则处理器
     * 在规则处理器中根据热点Key没有找到时，如果有设置默认的规则处理器
     * 都会校验默认的规则处理器判断是否满足规则
     *
     * @param rule           规则
     * @param triggerInvoker 规则触发器
     * @param <T>            T
     */
    <T extends TriggerRule> void registerDefaultInvoker(T rule, TriggerInvoker<T> triggerInvoker);
}
