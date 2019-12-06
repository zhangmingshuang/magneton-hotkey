package com.magneton.hotkey.server.trigger;

import com.magneton.hotkey.common.Hotkey;
import com.magneton.hotkey.server.storager.MemoryHotkeyStorager;
import com.magneton.hotkey.server.trigger.rule.NumberTriggerRule;
import com.magneton.hotkey.server.trigger.rule.TriggerRule;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/5
 */
public class DefaultHotkeyTriggerTest {

    @Test
    public void test() {
        String testKey = "test";

        DefaultHotkeyTrigger trigger = new DefaultHotkeyTrigger();
        trigger.registerStorager(new MemoryHotkeyStorager());
        trigger.registerInvoker("test", new NumberTriggerRule(1), new TriggerInvoker<NumberTriggerRule>() {
            @Override
            public void invoke(String key, NumberTriggerRule triggerRule) {
                System.out.println(key);
                Assert.assertEquals(key, testKey);
                Assert.assertTrue(triggerRule.getNumber() == 1);
            }
        });
        trigger.init();
        trigger.listen(Hotkey.of(testKey, "test"));
    }
}
