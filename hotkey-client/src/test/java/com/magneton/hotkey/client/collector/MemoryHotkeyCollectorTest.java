package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.client.config.CollectConfig;
import com.magneton.hotkey.client.support.TestHotkeySummarier;
import com.magneton.hotkey.common.Hotkey;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class MemoryHotkeyCollectorTest {

    private boolean summary = false;
    private TestHotkeySummarier hotkeySummarier = new TestHotkeySummarier();

    @Test
    public void test() {
        CollectConfig properties = new CollectConfig();
        properties.setMaximumSize(1);
        HotkeyCollector collector = this.createHotkeyCollector(properties);
        collector.fire(Hotkey.of("key", "value"));
        collector.fire(Hotkey.of("key", "value"));
        Assert.assertTrue(summary);
        HotkeyContainer hotkeyContainer = hotkeySummarier.getHotkeyContainer();
        Assert.assertNotNull(hotkeyContainer);
        Assert.assertTrue(hotkeyContainer.size() == 1);
    }

    private HotkeyCollector createHotkeyCollector(CollectConfig properties) {
        AbstractHotkeyCollector collector = new MemoryHotkeyCollector() {
            @Override
            protected void summary() {
                summary = true;
                super.summary();
            }
        };
        collector.setCollectConfig(properties);
        collector.setHotkeySummarier(hotkeySummarier);
        collector.init();
        return collector;
    }
}
