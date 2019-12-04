package com.magneton.hotkey.server.invoker;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public interface HotkeyInvokerRegister {

    void registerInvoker(String key, InvokerRule rule, HotkeyInvoker invoker);

    HotkeyListener getHotkeyListener();

    void registerRulePostProcess(InvokerRulePostProcess invokerRulePostProcess);

}
