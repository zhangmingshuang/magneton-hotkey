package com.magneton.hotkey.common;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
@Setter
@Getter
@ToString
public class Hotkey implements Serializable {

    private long time;
    /**
     * 热点Key，应该表示的是一种热点规则
     */
    private String key;
    /**
     * 热点值，表示具体的规则数据
     */
    private String value;

    public static final Hotkey of(String key, String value, long time) {
        Hotkey hotkey = new Hotkey();
        hotkey.key = key;
        hotkey.value = value;
        hotkey.time = time;
        return hotkey;
    }

    public static final Hotkey of(String key, String value) {
        return of(key, value, System.currentTimeMillis());
    }
}
