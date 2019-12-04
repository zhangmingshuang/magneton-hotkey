package com.magneton.hotkey.server.consumer;

import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public interface HotkeyConsumer {

    boolean hold(Hotkey hotkey);
}
