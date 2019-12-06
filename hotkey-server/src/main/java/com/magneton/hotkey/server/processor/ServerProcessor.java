package com.magneton.hotkey.server.processor;

import com.magneton.hotkey.server.HotkeyListener;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public interface ServerProcessor {

    void registerListener(HotkeyListener hotkeyListener);

    boolean removeListener(HotkeyListener hotkeyListener);

    void regiseterHotkeyPostProcess(HotkeyPostProcess hotkeyPostProcess);

    void start();
}
