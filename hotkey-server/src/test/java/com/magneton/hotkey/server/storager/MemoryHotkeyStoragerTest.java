package com.magneton.hotkey.server.storager;

import com.magneton.hotkey.common.Hotkey;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class MemoryHotkeyStoragerTest {


    @Test
    public void test() {
        String key = "test";
        MemoryHotkeyStorager storager = new MemoryHotkeyStorager();
        storager.registerSaveProcess(new StorageProcess() {
            @Override
            public boolean before(StorageConnection connection, Hotkey hotkey) {
                return true;
            }

            @Override
            public void after(StorageConnection connection, Hotkey hotkey) {
                StorageData data = connection.getData(hotkey.getKey());
                Assert.assertTrue(data.getKeyTriggerCount() == 1);
                long time = 123;
                boolean update = connection.updateCursorTime(hotkey.getKey(), time);
                Assert.assertTrue(update);

                StorageData data1 = connection.getData(hotkey.getKey());
                Assert.assertTrue(data1.getCursorTime() == time);

                update = connection.incrKeyTriggerCount(hotkey.getKey(), 111);
                Assert.assertTrue(update);

                StorageData data2 = connection.getData(hotkey.getKey());
                Assert.assertTrue(data2.getKeyTriggerCount() == 112);

            }
        });
        storager.save(Hotkey.of(key, "value"));
    }

    @Test
    public void testProcessIntercept() {
        String key = "testProcessIntercept";

        MemoryHotkeyStorager storager = new MemoryHotkeyStorager();
        storager.registerSaveProcess(new StorageProcess() {
            @Override
            public boolean before(StorageConnection connection, Hotkey hotkey) {
                System.out.println(hotkey);
                return false;
            }

            @Override
            public void after(StorageConnection connection, Hotkey hotkey) {
                Assert.assertTrue(false);
                System.out.println("after:" + hotkey);
            }
        });
        storager.save(Hotkey.of(key, "value"));
    }
}
