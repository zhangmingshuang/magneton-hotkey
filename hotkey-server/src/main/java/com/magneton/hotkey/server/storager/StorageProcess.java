package com.magneton.hotkey.server.storager;

import com.magneton.hotkey.common.Hotkey;

/**
 * @author zhangmingshuang
 * @since 2019/12/6
 */
public interface StorageProcess<T extends StorageConnection> {

    default boolean before(T connection, Hotkey hotkey) {
        return true;
    }

    default void after(T connection, Hotkey hotkey) {

    }
}
