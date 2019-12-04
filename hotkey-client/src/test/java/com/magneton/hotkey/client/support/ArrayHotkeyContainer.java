package com.magneton.hotkey.client.support;

import com.magneton.hotkey.client.HotkeyContainer;
import com.magneton.hotkey.common.Hotkey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class ArrayHotkeyContainer implements HotkeyContainer {

    private List<Hotkey> list = new ArrayList();

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean offer(Hotkey hotkey) {
        return list.add(hotkey);
    }

    @Override
    public Hotkey[] getHotkeys() {
        return list.toArray(new Hotkey[0]);
    }
}
