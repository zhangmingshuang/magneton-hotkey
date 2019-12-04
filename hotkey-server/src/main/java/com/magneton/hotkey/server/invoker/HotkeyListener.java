package com.magneton.hotkey.server.invoker;

import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public interface HotkeyListener {

    void fire(Hotkey hotkey);
}
