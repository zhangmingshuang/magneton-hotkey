package com.magneton.hotkey.client.collector;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class MemoryHotkeyCollector extends AbstractHotkeyCollector {

    @Override
    protected HotkeyContainer getHotkeyContainer() {
        int capacity = (int) (super.getMaximumSize() * 1.4);
        return new BlockQueueHotkeyContainer(capacity);
    }
}
