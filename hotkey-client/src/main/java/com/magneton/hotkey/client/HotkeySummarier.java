package com.magneton.hotkey.client;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public interface HotkeySummarier<T> {


    void setProperties(T properties);

    void afterPropertiesSet();

    void report(HotkeyContainer hotkeyContainer);
}
