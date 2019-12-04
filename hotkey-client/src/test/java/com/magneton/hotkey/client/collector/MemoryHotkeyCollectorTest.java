package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.client.HotkeyContainer;
import com.magneton.hotkey.client.properties.CollectProperties;
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
        CollectProperties properties = new CollectProperties();
        properties.setMaximumSize(1);
        HotkeyCollector collector = this.createHotkeyCollector(properties);
        collector.fire(Hotkey.of("key", "value"));
        collector.fire(Hotkey.of("key", "value"));
        Assert.assertTrue(summary);
        HotkeyContainer hotkeyContainer = hotkeySummarier.getHotkeyContainer();
        Assert.assertNotNull(hotkeyContainer);
        Assert.assertTrue(hotkeyContainer.size() == 1);
    }

    private HotkeyCollector createHotkeyCollector(CollectProperties properties) {
        AbstractHotkeyCollector collector = new MemoryHotkeyCollector() {
            @Override
            protected void summary() {
                summary = true;
                super.summary();
            }
        };
        collector.setProperties(properties);
        collector.afterPropertiesSet();
        collector.setHotkeySummarier(hotkeySummarier);
        return collector;
    }
}
