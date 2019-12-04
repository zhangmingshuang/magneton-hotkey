package com.magneton.hotkey.client.collector;


import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public interface HotkeyCollector<T extends Hotkey> {

    boolean fire(T hotkey);
}
