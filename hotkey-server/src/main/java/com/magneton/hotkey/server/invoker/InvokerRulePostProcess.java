package com.magneton.hotkey.server.invoker;

import com.magneton.hotkey.server.invoker.MemoryHotkeyInvokerRegister.Invoker;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public interface InvokerRulePostProcess {

    Class<? extends InvokerRule> interest();

    boolean isAccept(Invoker invoker);
}
