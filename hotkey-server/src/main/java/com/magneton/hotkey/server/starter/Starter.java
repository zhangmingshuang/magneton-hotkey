package com.magneton.hotkey.server.starter;

import com.magneton.hotkey.server.consumer.HotkeyConsumer;
import com.magneton.hotkey.server.invoker.HotkeyInvokerRegister;
import java.util.concurrent.Executor;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public interface Starter<T> {

    void setProperties(T properties);

    void afterPropertiesSet();

    void registerDefaultConsumer(HotkeyConsumer hotkeyConsumer);

    void registerConsumer(String key, HotkeyConsumer hotkeyConsumer);

    HotkeyInvokerRegister getHotkeyInvokerRegister();

    void setExecutor(Executor executor);
}
