package com.magneton.hotkey.common;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * [length, time, body]
 *
 * @author zhangmingshuang
 * @since 2019/12/3
 */
@Setter
@Getter
@ToString
public class Hotkey implements Serializable {

    private long time;
    private String key;
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
