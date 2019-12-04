package com.magneton.hotkey.client;

import com.magneton.hotkey.client.collector.HotkeyCollector;
import com.magneton.hotkey.client.collector.MemoryHotkeyCollector;
import com.magneton.hotkey.client.properties.CollectProperties;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import lombok.Setter;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
@Setter
public class HotkeyCollectorBuilder {

    private CollectProperties collectProperties;
    private Executor executor;

    public HotkeyCollector build() {
        MemoryHotkeyCollector memoryHotkeyCollector = new MemoryHotkeyCollector();
        memoryHotkeyCollector.setExecutor(executor);
        memoryHotkeyCollector.setProperties(collectProperties);
        memoryHotkeyCollector.afterPropertiesSet();
        return memoryHotkeyCollector;
    }

    public HotkeyCollectorBuilder setExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }
}
