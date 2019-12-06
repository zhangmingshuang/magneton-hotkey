package com.magneton.hotkey.server;

import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public interface HotkeyListener {

    /**
     * 监听器排序顺序
     *
     * @return 顺序值，值越大，则越先调用
     */
    default int order() {
        return 0;
    }

    /**
     * 监听事件
     *
     * @param hotkey 监听数据
     */
    void listen(Hotkey hotkey);
}
