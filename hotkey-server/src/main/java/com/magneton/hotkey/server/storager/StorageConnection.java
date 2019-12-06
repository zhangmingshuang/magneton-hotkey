package com.magneton.hotkey.server.storager;

import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
public interface StorageConnection {

    /**
     * 保存数据
     */
    void save(Hotkey hotkey);

    StorageData getData(String key);

    boolean updateCursorTime(String key, long time);

    boolean incrKeyTriggerCount(String key, int incr);
}
