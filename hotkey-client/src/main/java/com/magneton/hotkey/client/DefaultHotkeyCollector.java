package com.magneton.hotkey.client;

import com.magneton.hotkey.client.collector.ConfigurableHotkeyCollector;
import com.magneton.hotkey.client.collector.HotkeyCollector;
import com.magneton.hotkey.client.collector.MemoryHotkeyCollector;
import com.magneton.hotkey.client.config.CollectConfig;
import com.magneton.hotkey.client.summarier.BoltHotkeySummarier;
import com.magneton.hotkey.client.summarier.BoltSummarierConfig;
import com.magneton.hotkey.common.Hotkey;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.experimental.Delegate;

/**
 * @author zhangmingshuang
 * @since 2019/12/4
 */
public class DefaultHotkeyCollector implements HotkeyCollector {

    @Delegate
    private CollectConfig collectConfig = new CollectConfig();
    @Delegate
    private BoltSummarierConfig boltSummarierConfig = new BoltSummarierConfig();
    private Executor executor;
    private BoltHotkeySummarier hotkeySummarier;
    private ConfigurableHotkeyCollector hotkeyCollector;
    private AtomicBoolean running = new AtomicBoolean(false);

    public void start() {
        if (!running.compareAndSet(false, true)) {
            return;
        }
        //初始化
        hotkeySummarier = new BoltHotkeySummarier(boltSummarierConfig);
        hotkeySummarier.init();
        //默认内存采集器
        if (hotkeyCollector == null) {
            hotkeyCollector = new MemoryHotkeyCollector();
        }
        hotkeyCollector.setCollectConfig(collectConfig);
        hotkeyCollector.setExecutor(executor);
        hotkeyCollector.setHotkeySummarier(hotkeySummarier);
        hotkeyCollector.init();
    }

    @Override
    public boolean fire(Hotkey hotkey) {
        if (!running.get()) {
            throw new RuntimeException("fire must be after start.");
        }
        return hotkeyCollector.fire(hotkey);
    }

    public void setExecutor(Executor executor) {
        this.validateBeforeStart();
        this.executor = executor;
    }

    public void setBoltSummarierConfig(BoltSummarierConfig boltSummarierConfig) {
        this.validateBeforeStart();
        this.boltSummarierConfig = boltSummarierConfig;
    }

    public void setCollectConfig(CollectConfig collectConfig) {
        this.validateBeforeStart();
        this.collectConfig = collectConfig;
    }

    private void validateBeforeStart() {
        if (running.get()) {
            throw new RuntimeException("config must be before start.");
        }
    }
}
