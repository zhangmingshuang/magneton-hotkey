package com.magneton.hotkey.server.demo.consumer;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.consumer.HotkeyConsumer;
import org.springframework.stereotype.Component;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@HotkeyComponent("test")
public class TestConsunmer implements HotkeyConsumer {

    @Override
    public boolean hold(Hotkey hotkey) {
        System.out.println(hotkey);
        return true;
    }
}
