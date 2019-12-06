package com.magneton.hotkey.server;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public interface ConfigableHotkeyServer<T> extends HotkeyServer<T> {

    void setServerProcessor(T serverProcessor);

    void prepareContext();

}
