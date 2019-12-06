package com.magneton.hotkey.client.collector;

import com.magneton.hotkey.client.config.CollectConfig;
import com.magneton.hotkey.client.support.ArrayHotkeyContainer;
import com.magneton.hotkey.client.support.TestHotkeySummarier;
import com.magneton.hotkey.common.Hotkey;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhangmingshuang
 * @since 2019/12/3
 */
public class AbstractHotkeyCollectorTest {

    private boolean summary = false;
    private TestHotkeySummarier hotkeySummarier = new TestHotkeySummarier();

    @Test
    public void test() {
        CollectConfig collectConfig = new CollectConfig();
        collectConfig.setMaximumSize(1);
        HotkeyCollector collector = this.createHotkeyCollector(collectConfig);
        collector.fire(Hotkey.of("key", "value"));
        collector.fire(Hotkey.of("key", "value"));
        Assert.assertTrue(summary);
        HotkeyContainer hotkeyContainer = hotkeySummarier.getHotkeyContainer();
        Assert.assertNotNull(hotkeyContainer);
        Assert.assertTrue(hotkeyContainer.size() == 1);
    }

    private HotkeyCollector createHotkeyCollector(CollectConfig collectConfig) {
        AbstractHotkeyCollector collector = new AbstractHotkeyCollector() {
            @Override
            protected HotkeyContainer getHotkeyContainer() {
                return new ArrayHotkeyContainer();
            }

            @Override
            protected void summary() {
                summary = true;
                super.summary();
            }
        };
        collector.setCollectConfig(collectConfig);
        collector.init();
        collector.setHotkeySummarier(hotkeySummarier);
        return collector;
    }
}
