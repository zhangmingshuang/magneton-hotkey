package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.common.Hotkey;
import java.util.concurrent.ArrayBlockingQueue;

public class BlockQueueHotkeyContainer extends ArrayBlockingQueue
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