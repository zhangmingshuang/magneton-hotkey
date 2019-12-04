package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.client.HotkeyContainer;
import com.magneton.hotkey.common.Hotkey;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class MemoryHotkeyCollector extends AbstractHotkeyCollector {

    public static class BlockQueueHotkeyContainer extends ArrayBlockingQueue
        implements HotkeyContainer {

        public BlockQueueHotkeyContainer(int capacity) {
            super(capacity);
        }

        @Override
        public boolean offer(Hotkey hotkey) {
            return super.offer(hotkey);
        }

        @Override
        public Hotkey[] getHotkeys() {
            return (Hotkey[]) super.toArray(new Hotkey[0]);
        }
    }

    @Override
    protected HotkeyContainer getHotkeyContainer() {
        int capacity = (int) (super.getMaximumSize() * 1.4);
        return new BlockQueueHotkeyContainer(capacity);
    }
}
