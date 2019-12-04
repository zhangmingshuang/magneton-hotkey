package com.magneton.hotkey.client;

import com.magneton.hotkey.common.Hotkey;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class HotkeyTest {

    @Test
    public void test() {
        Hotkey hotkey = Hotkey.of("key", "value");
        Assert.assertEquals("key", hotkey.getKey());
        Assert.assertEquals("value", hotkey.getValue());
        Assert.assertTrue(hotkey.getTime() > System.currentTimeMillis() - 100);

        Hotkey withTime = Hotkey.of("key", "value", 1);
        Assert.assertEquals("key", withTime.getKey());
        Assert.assertEquals("value", withTime.getValue());
        Assert.assertTrue(withTime.getTime() == 1);
    }
}
