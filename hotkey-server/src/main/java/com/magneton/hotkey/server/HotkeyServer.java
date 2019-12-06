package com.magneton.hotkey.server;

import java.util.function.Consumer;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public interface HotkeyServer<T> {

    void start();

    void afterStart(Consumer<T> consumer);
}
