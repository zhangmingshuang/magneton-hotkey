package com.magneton.hotkey.server.demo.consumer;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.consumer.HotkeyConsumer;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@HotkeyComponent("*")
public class DefaultConsumer implements HotkeyConsumer {

    @Override
    public boolean hold(Hotkey hotkey) {
        System.out.println("default--->" + hotkey);
        return true;
    }
}
