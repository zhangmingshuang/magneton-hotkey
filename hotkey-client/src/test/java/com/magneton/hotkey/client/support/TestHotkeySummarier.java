package com.magneton.hotkey.client.support;

import com.magneton.hotkey.client.collector.HotkeyContainer;
import com.magneton.hotkey.client.summarier.HotkeySummarier;
import lombok.Getter;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class TestHotkeySummarier implements HotkeySummarier {

    @Getter
    private HotkeyContainer hotkeyContainer;

    @Override
    public void report(HotkeyContainer hotkeyContainer) {
        this.hotkeyContainer = hotkeyContainer;
    }
}
