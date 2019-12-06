package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.common.Hotkey;

/**
 * 热键容器，用来保存热键数据
 *
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public interface HotkeyContainer {

    int size();

    boolean offer(Hotkey hotkey);

    Hotkey[] getHotkeys();
}
