package com.magneton.hotkey.client.support;

import com.magneton.hotkey.client.HotkeyContainer;
import com.magneton.hotkey.client.HotkeySummarier;
import lombok.Getter;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class TestHotkeySummarier implements HotkeySummarier {

    @Getter
    private HotkeyContainer hotkeyContainer;

    @Override
    public void setProperties(Object properties) {

    }

    @Override
    public void afterPropertiesSet() {

    }

    @Override
    public void report(HotkeyContainer hotkeyContainer) {
        this.hotkeyContainer = hotkeyContainer;
    }
}
