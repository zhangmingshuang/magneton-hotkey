package com.magneton.hotkey.server.processor;

import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public interface HotkeyPostProcess {

    boolean before(Hotkey hotkey);
}
